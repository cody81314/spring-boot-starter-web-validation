package tw.com.ruten.springbootstarterwebvalidation.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface Validate {

    /**
     * 定義正則表示式來檢核資料格式 (Type 為 String 時適用)
     */
    String pattern() default "";

    /**
     * 是否允許null
     */
    boolean notNull() default true;

    /**
     * 實作 ValidationProvider 介面來定義複雜檢核邏輯，指定 ValidationProvider id
     */
    String[] providers() default {};

    /**
     * 若物件為多個參數的包裝，本身不需要執行驗證，但其包裝的屬性需要驗證，則設為true
     */
    boolean wrapper() default false;
}
