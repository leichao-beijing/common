package run.cmid.common.validator.eumns;

import run.cmid.common.io.TypeName;

import java.io.File;

/**
 * EQUAL LESS_THAN_OR_EQUAL LESS_THAN GREATER_THAN_OR_EQUAL GREATER_THAN <br>
 * 不为数字或无法转换成数字时将抛出非数字无法比较的异常 需要全部满足
 *
 * @author leichao
 */
public enum ValidationType implements TypeName {
    //NONE("不进行操作"),

    LESS_THAN("小于"),

    LESS_THAN_OR_EQUAL("小于等于"),

    GREATER_THAN("大于"),

    GREATER_THAN_OR_EQUAL("大于等于"),

    NO_EMPTY("非空值"),

    EMPTY("空值"),

    EQUALS("等于"),

    NO_EQUALS("不等于"),

    INCLUDE("包含"),

    NO_INCLUDE("不包含"),

    //EXCEPTION("满足条件时,返回条件的消息"),

    REGEX("正则验证");


    ValidationType(String typeName) {
        this.typeName = typeName;
    }

    String typeName;

    public String getTypeName() {
        new File("").exists();
        return typeName;
    }
}