package tw.com.ruten.springbootstarterwebvalidation.validator;

import tw.com.ruten.springbootstarterwebvalidation.error.ValidationException;
import tw.com.ruten.springbootstarterwebvalidation.validator.annotation.Validate;

public class NonNullableValidationProvider implements ValidationProvider {

    @Override
    public void validate(Object target, String targetName, Validate annotation) throws ValidationException {
        if (target == null) {
            throw new ValidationException(String.format("Field: [%s] should not be null", targetName));
        }
    }
}
