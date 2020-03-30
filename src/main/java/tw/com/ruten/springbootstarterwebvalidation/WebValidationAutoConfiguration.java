package tw.com.ruten.springbootstarterwebvalidation;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.com.ruten.springbootstarterwebvalidation.aop.aspect.ValidateRequestAspect;
import tw.com.ruten.springbootstarterwebvalidation.validator.NonNullableValidationProvider;
import tw.com.ruten.springbootstarterwebvalidation.validator.StringPatternValidationProvider;
import tw.com.ruten.springbootstarterwebvalidation.validator.ValidationProvider;

@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@ConditionalOnMissingBean({ ValidateRequestAspect.class })
public class WebValidationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "stringPatternValidationProvider")
    public ValidationProvider stringPatternValidationProvider() {
        return new StringPatternValidationProvider();
    }

    @Bean
    @ConditionalOnMissingBean(name = "nonNullableValidationProvider")
    public ValidationProvider nonNullableValidationProvider() {
        return new NonNullableValidationProvider();
    }

    @Bean
    public ValidateRequestAspect validateRequestAspect() {
        return new ValidateRequestAspect();
    }
}
