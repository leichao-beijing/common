package run.cmdi.common.poi.core;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

public class SheetComment {
    public final CellStyle whiteBorder;
    public final CellStyle yellowBorder;
    public final CellStyle redBorder;

    private final Sheet sheet;

    public SheetComment(Sheet sheet) {

        this.sheet = sheet;
        whiteBorder = getWhiteBorder();
        yellowBorder = getYellowBorder();
        redBorder = getRedBorder();

    }

//    public Comment getComment(String maker, String value) throws IOException {
//        if (sheet instanceof HSSFSheet)
//            return commentXssf((HSSFSheet) sheet, maker, value);
//        if (sheet instanceof XSSFSheet)
//            return commentHssf((XSSFSheet) sheet, maker, value);
//        throw new IOException("NO Sheet Instanceof");
//    }

    public void createComment(String maker, String value, Cell cell) throws IOException {
        if (sheet instanceof HSSFSheet)
            cell.setCellComment(
                    commentXssf((HSSFSheet) sheet, maker, value, (short) cell.getColumnIndex(), cell.getRowIndex()));
        if (sheet instanceof XSSFSheet)
            cell.setCellComment(
                    commentHssf((XSSFSheet) sheet, maker, value, (short) cell.getColumnIndex(), cell.getRowIndex()));
    }

    private Comment commentXssf(HSSFSheet sheet, String maker, String value, short col, int row) {
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, col, row, (short) (col + 2), row + 2);
        HSSFPatriarch patriarch = sheet.getDrawingPatriarch();
        if (patriarch == null)
            patriarch = sheet.createDrawingPatriarch();
        HSSFComment comment = patriarch.createCellComment(anchor);
        comment.setAuthor(maker);
        setRichTextString(comment, value);
        return comment;
    }

    public void setRichTextString(Comment comment, String value) {
        if (comment instanceof HSSFComment)
            setRichTextStringHssf(comment, value);
        if (comment instanceof XSSFComment)
            setRichTextStringXssf(comment, value);
    }

    public void setRichTextStringHssf(Comment comment, String value) {
        comment.setString(new HSSFRichTextString(value));
    }

    public void setRichTextStringXssf(Comment comment, String value) {
        comment.setString(new XSSFRichTextString(value));
    }

    private Comment commentHssf(XSSFSheet sheet, String maker, String value, short col, int row) {
        ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, col, row, (short) (col + 2), row + 2);
        XSSFDrawing patriarch = sheet.getDrawingPatriarch();
        if (patriarch == null)
            patriarch = sheet.createDrawingPatriarch();
        XSSFComment comment = patriarch.createCellComment(anchor);
        comment.setAuthor(maker);
        setRichTextString(comment, value);
        return comment;
    }

    /**
     * 清除单元格样式
     */
    public void clearStyleCell(Cell cell) {
        cell.removeCellComment();
        cell.setCellStyle(null);
    }

    /**
     * 清除单元格的样式
     */
    public void clearStyleCell(int rownum, int column) {
        Optional<Cell> cellOptional = SheetUtils.getCell(sheet, rownum, column);
        if (!cellOptional.isEmpty()) {
            Cell cell = cellOptional.get();
            clearStyleCell(cell);
        }
    }

    /**
     * 清除指定行的样式
     */
    public void clearStyleRow(int rownum) {
        Optional<Row> rowOptional = SheetUtils.getRow(sheet, rownum);
        if (!rowOptional.isEmpty()) {
            Row row = rowOptional.get();
            Iterator<Cell> it = row.cellIterator();
            while (it.hasNext()) {
                Cell cell = it.next();
                clearStyleCell(cell);
            }
        }
    }

    /**
     * 清除列的全部样式
     */
    public void clearStyleColumn(int column) {
        int rowCount = SheetUtils.getRowCount(sheet);
        for (int i = 0; i < rowCount; i++) {
            clearStyleCell(i, column);
        }
    }

    /**
     * 获得重新配置好字体的cellStyle颜色 白色
     */
    public CellStyle whiteBorder(CellStyle style) {
        copyFont(style, whiteBorder);
        return whiteBorder;
    }

    /**
     * 获得重新配置好字体的cellStyle颜色 红色
     */
    public CellStyle redBorder(CellStyle style) {
        copyFont(style, redBorder);
        return redBorder;
    }

    /**
     * 获得重新配置好字体的cellStyle颜色 黄色
     */
    public CellStyle yellowBorder(CellStyle style) {
        copyFont(style, yellowBorder);
        return yellowBorder;
    }

    /**
     * 获取 白色 待边框style配置
     */
    private CellStyle getWhiteBorder() {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        setBorderStyle(style);
        setColorStyle(style, IndexedColors.WHITE1);
        return style;
    }

    /**
     * 获取 黄色 待边框style配置
     */
    private CellStyle getYellowBorder() {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        setBorderStyle(style);
        setColorStyle(style, IndexedColors.YELLOW1);
        return style;
    }

    /**
     * 获取 黄色 待边框style配置
     */
    private CellStyle getRedBorder() {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        setBorderStyle(style);
        setColorStyle(style, IndexedColors.RED1);
        return style;
    }

    /**
     * 设置默认边框
     */
    public void setBorderStyle(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
    }

    /**
     * 设置无填充样式
     */
    public void setNoFillStyle(CellStyle Style) {
        Style.setFillPattern(FillPatternType.NO_FILL);// 无填充颜色样式
    }

    /**
     * 设置颜色
     */
    public void setColorStyle(CellStyle style, IndexedColors color) {
        style.setFillBackgroundColor(color.getIndex());
        style.setFillForegroundColor(color.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * 复制字体
     */
    public void copyFont(CellStyle srcStyle, CellStyle desStyle) {
        Font font = getFont(srcStyle);
        desStyle.setFont(font);
    }

    /**
     * 获取兼容格式的Font字体对象
     */
    public Font getFont(CellStyle srcStyle) {
        if (srcStyle instanceof HSSFCellStyle) {
            HSSFCellStyle hcs = (HSSFCellStyle) srcStyle;
            return hcs.getFont(sheet.getWorkbook());
        } else {
            XSSFCellStyle xcs = (XSSFCellStyle) srcStyle;
            return xcs.getFont();
        }
    }

}
