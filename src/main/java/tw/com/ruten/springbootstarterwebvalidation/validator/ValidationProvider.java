package tw.com.ruten.springbootstarterwebvalidation.validator;

import tw.com.ruten.springbootstarterwebvalidation.error.ValidationException;
import tw.com.ruten.springbootstarterwebvalidation.validator.annotation.Validate;

public interface ValidationProvider<T> {

    void validate(T target, String targetName, Validate annotation) throws ValidationException;
}
