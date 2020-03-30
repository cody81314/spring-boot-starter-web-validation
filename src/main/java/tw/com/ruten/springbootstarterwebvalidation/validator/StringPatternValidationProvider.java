package tw.com.ruten.springbootstarterwebvalidation.validator;

import tw.com.ruten.springbootstarterwebvalidation.error.ValidationException;
import tw.com.ruten.springbootstarterwebvalidation.validator.annotation.Validate;

public class StringPatternValidationProvider implements ValidationProvider<String> {

    @Override
    public void validate(String target, String targetName, Validate annotation) throws ValidationException {
        String pattern = annotation.pattern();

        if (target == null) {
            return;
        }

        if (!target.matches(pattern)) {
            throw new ValidationException(
                    String.format("Field: [%s] with value [%s] not match regular expression \"%s\"",
                            targetName, target, pattern));
        }
    }
}
