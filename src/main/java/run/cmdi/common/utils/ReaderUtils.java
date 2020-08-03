package run.cmdi.common.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import run.cmdi.common.reader.core.EntityBuild;
import run.cmdi.common.reader.model.entity.EntityResults;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.service.ExcelEntityBuildings;

import java.io.IOException;
import java.io.InputStream;

public class ReaderUtils<T> {
    private Class<T> clazz;
    private InputStream is;

    public ReaderUtils(Class<T> clazz, InputStream is) {
        this.clazz = clazz;
        this.is = is;
    }

    public EntityBuild<T, Sheet, Cell> build() throws IOException, ConverterException {
        return build(0);
    }

    public EntityBuild<T, Sheet, Cell> build(int readRow) throws IOException, ConverterException {
        ExcelEntityBuildings<T> ee = new ExcelEntityBuildings<T>(clazz);
        Workbook workbook = new XSSFWorkbook(is);
        return ee.find(workbook, readRow);
    }

    public EntityResults<T, Sheet, Cell> result(int readRow) throws IOException, ConverterException {
        return build(readRow).build();
    }

    public EntityResults<T, Sheet, Cell> result() throws IOException, ConverterException {
        return result(0);
    }

    public static InputStream getResourceAsStream(String str) {
        return ClassLoader.getSystemResourceAsStream(str);
    }
}