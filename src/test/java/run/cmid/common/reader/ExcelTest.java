package run.cmid.common.reader;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import run.cmid.common.poi.core.PoiReader;
import run.cmid.common.poi.core.SheetComment;
import run.cmid.common.poi.model.ReaderPoiConfig;
import run.cmid.common.poi.model.StyleInfo;
import run.cmid.common.reader.core.EntityBuild;
import run.cmid.common.reader.core.ReaderPage;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.DemandTable;
import run.cmid.common.reader.model.ProduceTable;
import run.cmid.common.reader.model.Project;
import run.cmid.common.reader.model.entity.EntityResult;
import run.cmid.common.reader.service.ExcelEntityBuildings;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author leichao
 */

public class ExcelTest {

    @Test
    public void testProject() throws IOException, ConverterExcelException {
        //InputStream ras = getClass().getClassLoader().getResourceAsStream("data/testDemand-1.xls");
        InputStream ras =  new FileInputStream("C:\\Users\\leichao\\Desktop\\产值工具\\app\\错误数据\\to雷超-项目表.xlsx") ;
        ExcelEntityBuildings<Project> ee = new ExcelEntityBuildings<Project>(Project.class);
        Workbook workbook = new XSSFWorkbook(ras);
        EntityBuild<Project> e = ee.find(workbook);
        EntityResult<Project> result = e.build();

        result.getErrorType().forEach((ss) -> {
            System.err.println(ss);
        });
        result.getCellErrorList().forEach((value) -> {
            System.err.println(value + ">>" + value.getMessage());

        });
        result.getResultList().forEach((var) -> {
            System.err.println(var);
        });
    }

    @Test
    public void poiReaderClone() throws IOException {
        InputStream ras = getClass().getClassLoader().getResourceAsStream("data/testDemand-1.xls");
        ReaderPoiConfig readerPoiConfig = new ReaderPoiConfig();
        PoiReader poi = PoiReader.build(ras, null, readerPoiConfig, null);
        poi.saveAndClose(new File("D:\\xxx.xls"));

        InputStream ras1 = getClass().getClassLoader().getResourceAsStream("data/produceTable.xlsx");
        ReaderPoiConfig readerPoiConfig1 = new ReaderPoiConfig();
        PoiReader poi1 = PoiReader.build(ras1, null, readerPoiConfig, null);

        poi.clone(poi1,"Sheet1","Sheet122222");
        File bigTable = new File("C:\\Users\\leichao\\Desktop\\产值工具\\大表测试.xlsx");
        ras= new FileInputStream(bigTable);
        ExcelReaderSheetBuilder cc = EasyExcel.read(ras).sheet(0);
//      System.err.println(cc.doReadSync());
//

//
//        poi = PoiReader.build(ras, null, readerPoiConfig, null);
//
//        ReaderPage<Sheet, Cell> book = poi.book(0);
//
//        for (int i = 0; i <book.length() ; i++) {
//            System.err.println(book.readRowList(i));
//        }
    }



    @Test
    public void poiReader() throws IOException {
        InputStream ras = getClass().getClassLoader().getResourceAsStream("data/testDemand-1.xls");
        ReaderPoiConfig readerPoiConfig = new ReaderPoiConfig();
        PoiReader poi = PoiReader.build(ras, null, readerPoiConfig, null);

        Sheet sheet = poi.getWorkbook().getSheetAt(0);
        SheetComment sd = new SheetComment(sheet);

        StyleInfo info = new StyleInfo();
        Color c = new Color(255, 255, 255);
        info.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        info.setFillForegroundColor(c);
        CellStyle style = poi.createStyle(info);
        Color color = poi.getColor(sheet.getRow(0).getCell(0).getCellStyle());
        Cell cell = sheet.getRow(0).getCell(2);
        cell.setCellStyle(style);
        Comment c1 = sd.getComment("", "c1");
        Comment c2 = sd.getComment("", "c2");
        cell.setCellComment(c1);
        cell.removeCellComment();
        cell.setCellComment(c2);
        cell = sheet.getRow(0).getCell(3);
        cell.setCellStyle(style);
        //cell.setCellComment(com);
        org.apache.poi.ss.usermodel.Color s = cell.getCellStyle().getFillForegroundColorColor();
        poi.saveAndClose(new File("D:\\xxx.xls"));
    }


    @Test
    public void test() throws IOException, ConverterExcelException {
        InputStream ras = getClass().getClassLoader().getResourceAsStream("data/testDemand-1.xls");
        ExcelEntityBuildings<DemandTable> ee = new ExcelEntityBuildings<DemandTable>(DemandTable.class);
        Workbook workbook = new HSSFWorkbook(ras);
        EntityBuild<DemandTable> e = ee.find(workbook);
        EntityResult<DemandTable> result = e.build();

        result.getErrorType().forEach((ss) -> {
            System.err.println(ss);
        });
        result.getCellErrorList().forEach((value) -> {
            System.err.println(value + ">>" + value.getMessage());

        });
        result.getResultList().forEach((var) -> {
            System.err.println(var);
        });
    }

    @Test
    public void produce() throws IOException, ConverterExcelException {
        InputStream ras = getClass().getClassLoader().getResourceAsStream("data/produceTable.xlsx");
        ExcelEntityBuildings<ProduceTable> ee = new ExcelEntityBuildings<ProduceTable>(ProduceTable.class);

        Workbook workbook = new XSSFWorkbook(ras);
        EntityBuild<ProduceTable> e = ee.find(workbook);
        EntityResult<ProduceTable> result = e.build();

        result.getErrorType().forEach((ss) -> {
            System.err.println(ss);
        });
        result.getCellErrorList().forEach((value) -> {
            System.err.println(value + ">>" + value.getMessage());

        });
        result.getResultList().forEach((var) -> {
            System.err.println(var);
        });
    }
}
