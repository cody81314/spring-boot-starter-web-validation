package tw.com.ruten.springbootstarterwebvalidation.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tw.com.ruten.springbootstarterwebvalidation.error.ValidationException;
import tw.com.ruten.springbootstarterwebvalidation.validator.ValidationProvider;
import tw.com.ruten.springbootstarterwebvalidation.validator.annotation.Validate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static tw.com.ruten.springbootstarterwebvalidation.constant.AOPConst.POINTCUT_WEBLAYER;
import static tw.com.ruten.springbootstarterwebvalidation.constant.ValidationConst.NOT_NULL_PROVIDER_ID;
import static tw.com.ruten.springbootstarterwebvalidation.constant.ValidationConst.STRING_PATTERN_PROVIDER_ID;

@Aspect
public class ValidateRequestAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateRequestAspect.class);

    @Autowired
    private Map<String, ValidationProvider> validationProviderMap;

    @Before(value = POINTCUT_WEBLAYER)
    public void validateRequest(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();

        IntStream.range(0, parameters.length).parallel()
                .forEach(idx -> {
                    Optional<Validate> validateAnnotationOpt =
                            Optional.ofNullable(parameters[idx].getAnnotation(Validate.class));

                    validateAnnotationOpt.ifPresent(validate -> {
                        validate(validate, args[idx], parameters[idx].getName());
                    });
                });
    }

    private void validate(Validate annotation, Object target, String targetName) {

        if (target != null) {
            if (Iterable.class.isAssignableFrom(target.getClass())) {
                Iterable iterable = (Iterable) target;
                Stream stream = StreamSupport.stream(iterable.spliterator(), true);

                stream.forEach(iteratedObj -> validate(annotation, iteratedObj, targetName));

                return;
            }
        }

        if (annotation.notNull())
            validationProviderMap.get(NOT_NULL_PROVIDER_ID).validate(target, targetName, annotation);

        if (StringUtils.hasText(annotation.pattern()))
            validationProviderMap.get(STRING_PATTERN_PROVIDER_ID).validate(target, targetName, annotation);

        Arrays.stream(annotation.providers()).parallel()
                .map(providerId -> validationProviderMap.get(providerId))
                .forEach(validationProvider -> validationProvider.validate(target, targetName, annotation));

        if (annotation.wrapper()) {
            Field[] declaredFields = target.getClass().getDeclaredFields();

            Arrays.stream(declaredFields).parallel()
                    .forEach(field -> {
                        Optional<Validate> validateAnnotationOpt = Optional.ofNullable(field.getAnnotation(Validate.class));
                        validateAnnotationOpt.ifPresent(validate -> {
                            field.setAccessible(true);
                            Object fieldValue = null;
                            try {
                                fieldValue = field.get(target);
                            } catch (IllegalAccessException e) {
                                String errorDesc = String.format("The validate field [%s] get value occur IllegalAccessException.",
                                        field.getName());
                                LOGGER.error(errorDesc, e);
                                throw new ValidationException(errorDesc);
                            }

                            validate(validate, fieldValue, field.getName());
                        });
                    });
        }
    }
}
