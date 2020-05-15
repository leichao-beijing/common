package run.cmid.common.excel.core;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.util.ReflectUtil;
import run.cmid.common.excel.annotations.ExcelConverter;
import run.cmid.common.excel.annotations.ExcelConverterList;
import run.cmid.common.excel.annotations.ExcelConverterSimple;
import run.cmid.common.excel.model.FieldDetail;
import run.cmid.common.excel.model.to.ExcelHeadModel;
import run.cmid.common.excel.model.to.FindSheetModel;

/**
 * @author leichao
 */
public class ConverterFieldDetail {
    public static <T> List<FieldDetail> toList(Class<T> classes, ExcelHeadModel excelHeadModel,
                                               List<String> indexes) {
        LinkedList<FieldDetail> list = new LinkedList<FieldDetail>();
        Field[] fields = ReflectUtil.getFields(classes);
        FieldDetail fieldDetail = null;
        boolean check = false;
        for (Field field : fields) {
            check = false;
            if (indexes!=null&&indexes.contains(field.getName()))
                check = true;
            JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
            ExcelConverter excelConverter = field.getAnnotation(ExcelConverter.class);
            ExcelConverterList excelConverterStringList = field.getAnnotation(ExcelConverterList.class);
            if (excelConverter != null && excelConverterStringList != null)
                throw new NullPointerException("@ExcelConverter and @ExcelConverterList Override");
            if (excelConverter != null) {
                fieldDetail = new FieldDetail(field, classes, jsonFormat, excelConverter);
                if (check)
                    fieldDetail.setNullCheck(true);
                list.add(fieldDetail);
                continue;
            }

            if (excelConverterStringList != null) {
                ExcelConverterSimple[] values = excelConverterStringList.value();
                if (values.length == 0) {
                    throw new NullPointerException("ExcelConverterList no data");
                }
                for (int i = 0; i < values.length; i++) {
                    fieldDetail = new FieldDetail(field, classes, jsonFormat, values[i], i);
                    if (check)
                        fieldDetail.setNullCheck(true);
                    list.add(fieldDetail);
                }
                continue;
            }
            if (excelHeadModel.isSkipNoAnnotationField())
                continue;
            fieldDetail = new FieldDetail(field, classes, jsonFormat);
            if (check)
                fieldDetail.setNullCheck(true);
            list.add(fieldDetail);
        }
        return list;

    }

    /**
     * 功能废弃
     */
    public static <T> FieldDetail addCheckInteface(FieldDetail fieldDetail, Field field) {
//        if (field.isAnnotationPresent(ExcelCheckStringRule.class))
//            fieldDetail.setStringRule(field.getAnnotation(ExcelCheckStringRule.class));
//        if (field.isAnnotationPresent(ExcelCheckEumnRule.class))
//            fieldDetail.setEumnRule(field.getAnnotation(ExcelCheckEumnRule.class));
        return fieldDetail;
    }

    public static <T> FindSheetModel<T> toFindModel(Class<T> classes, ExcelHeadModel excelHeadModel) {
        return new FindSheetModel<T>(toList(classes, excelHeadModel, null));
    }

    /**
     * 验证classes 中是否存在interfaceClasses接口类
     */
    public static boolean IsInterface(Class<?> classes, Class<?> interfaceClasses) {
        Class<?>[] is = classes.getInterfaces();
        if (classes.equals(interfaceClasses)) {
            return true;
        }
        for (Class<?> i : is) {
            if (i.equals(interfaceClasses))
                return true;
        }
        return false;
    }

    public static final Set<Class<?>> SupportClassesList = new HashSet<Class<?>>() {
        private static final long serialVersionUID = 1L;

        {
            add(String.class);

            add(int.class);
            add(Integer.class);

            add(double.class);
            add(Double.class);

            add(long.class);
            add(Long.class);

            add(float.class);
            add(Float.class);

            add(short.class);
            add(Short.class);

            add(boolean.class);
            add(Boolean.class);

            add(Date.class);
        }

    };

}
