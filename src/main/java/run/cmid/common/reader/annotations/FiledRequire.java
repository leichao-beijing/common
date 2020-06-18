package run.cmid.common.reader.annotations;

import run.cmid.common.reader.model.eumns.ExcelRead;

import java.lang.annotation.*;

/**
 * 生效field的条件
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FiledRequire {
    /**
     * 默认值：Master.filedName
     */
    String fieldName();

    String[] value();


    ExcelRead model() default ExcelRead.EQUALS;//regex

    /**
     * model== ExcelRead.NONE 时匹配正则 符合正则时生效。不存在时忽略 只验证转换对象的toString类型
     */
    String regex() default "";

    String message() default "";
}
