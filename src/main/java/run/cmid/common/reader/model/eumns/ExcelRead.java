package run.cmid.common.reader.model.eumns;

import run.cmid.common.io.TypeName;

import java.io.File;

/**
 * EQUAL LESS_THAN_OR_EQUAL LESS_THAN GREATER_THAN_OR_EQUAL GREATER_THAN <br>
 * 不为数字或无法转换成数字时将抛出非数字无法比较的异常 需要全部满足
 *
 * @author leichao
 */
public enum ExcelRead implements TypeName {
    NONE(""),

    LESS_THAN("小于"),

    LESS_THAN_OR_EQUAL("小于等于"),

    GREATER_THAN("大于"),

    GREATER_THAN_OR_EQUAL("大于等于"),

    EXISTS("非空值"),

    EMPTY("空值"),

    EQUALS("等于"),

    INCLUDE("包含"),

    NO_INCLUDE("不包含"),

    NO_EQUALS("不等于"),

    REGEX("正则验证");


    ExcelRead(String typeName) {
        this.typeName = typeName;
    }

    String typeName;

    public String getTypeName() {
        new File("").exists();
        return typeName;
    }
}
