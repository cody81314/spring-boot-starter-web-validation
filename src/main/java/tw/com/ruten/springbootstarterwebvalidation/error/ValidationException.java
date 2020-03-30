package tw.com.ruten.springbootstarterwebvalidation.error;

import lombok.Data;

@Data
public class ValidationException extends RuntimeException {

    private String errorDesc;

    public ValidationException(String errorDesc) {
        super(errorDesc);
        this.errorDesc = errorDesc;
    }
}
