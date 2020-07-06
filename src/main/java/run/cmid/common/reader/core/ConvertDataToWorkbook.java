package run.cmid.common.reader.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import run.cmid.common.io.StringUtils;
import run.cmid.common.reader.annotations.ConverterHead;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.eumns.FieldDetailType;
import run.cmid.common.reader.model.to.ExcelHeadModel;
import run.cmid.common.poi.core.SheetUtils;

/**
 * @author leichao
 */

public class ConvertDataToWorkbook<T, PAGE, UNIT> extends EntityBuildings<T, PAGE, UNIT> implements DataConvertInterface<T> {

    private final ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
    private final ArrayList<String> headNames = new ArrayList<String>();
    private final WorkbookInfo workbookInfo;
    private final HashMap<String, CellStyle> cellStyleMap = new HashMap<String, CellStyle>();
    private int rownum = 0;
    private Sheet sheet;
    private Map<String, FieldDetail> map;
    @Getter
    private boolean headState = false;
    @Getter
    private boolean state = false;
    private String sheetName;

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, Class<T> clazz)
            throws IOException, ConverterExcelException {
        this(workbookInfo, null, createFieldDetail(clazz), clazz);
    }

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, String sheetName, Class<T> clazz) throws IOException {
        this(workbookInfo, sheetName, createFieldDetail(clazz), clazz);
    }

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, String sheetName, Map<String, FieldDetail> map,
                                 Class<T> clazz) {
        super(clazz);
        this.workbookInfo = workbookInfo;
        this.sheetName = sheetName;
        headAndFieldDataList();
    }

    private void createSheet() {
        if (!headState) {
            try {
                workbookInfo.createFile();
                sheet = workbookInfo.createWorkbook().createSheet(sheetName);
            } catch (IOException e) {
                throw new NullPointerException(e.getMessage());
            }

            createHead();
            createCellStyle(sheet);
        }
        headState = true;
    }

    private void createCellStyle(Sheet sheet) {
        map.forEach((key, value) -> {
            if (value.getFormat() != null) {
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat(value.getFormat().value()));
                cellStyleMap.put(value.getFieldName(), cellStyle);
            }
        });
    }

    private static <T> Map<String, FieldDetail> createFieldDetail(Class<T> classes) {
        ConverterHead head = classes.getAnnotation(ConverterHead.class);
        if (head == null)
            throw new NullPointerException("@ExcelConverterHead not enable");
        isIndexMethod(head.indexes(), classes);// 验证index内值是否存在于对象中。
        ExcelHeadModel headModel = new ExcelHeadModel(head);
        return ConverterFieldDetail.toMap(classes, headModel, null);
    }

    /**
     * 分析FieldDetail<T> 获取 head和Field 名称List
     */
    private void headAndFieldDataList() {
        map.forEach((key, value) -> {
            headNames.add(value.getName() == null ? value.getFieldName() : value.getName());
        });
    }

    private void createHead() {
        for (int i = 0; i < headNames.size(); i++) {
            Cell cell = SheetUtils.getCreateCell(sheet, rownum, i);
            cell.setCellValue(headNames.get(i));
        }
        rownum++;
    }

    public void addObject(Object object) {
        if (!object.getClass().equals(getClazz())) {
            throw new NullPointerException("[" + getClazz() + " ]to[ " + object.getClass() + "] is error");
        }
        @SuppressWarnings("unchecked")
        T t = (T) object;
        add(t);
    }

    @Override
    public void add(T t) {
        createSheet();
        state = true;
        String str = null;
        int column = 0;
        Row row = SheetUtils.getCreateRow(sheet, rownum);
        Iterator<Map.Entry<String, FieldDetail>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, FieldDetail> next = it.next();
            FieldDetail tt = next.getValue();
            Object value = ReflectUtil.invoke(t, ("get" + StrUtil.upperFirst(tt.getFieldName())));
            if (value == null) {
                column++;
                continue;
            }
            Cell cell = SheetUtils.getCreateCell(row, column);
            CellStyle cellStyle = cellStyleMap.get(tt.getFieldName());
            if (cellStyle != null)
                cell.setCellStyle(cellStyle);
            if (tt.getType() == FieldDetailType.LIST) {
                @SuppressWarnings("unchecked")
                List<Object> list = (List<Object>) value;
                try {
                    Object string = list.get(tt.getIndex());
                    if (StringUtils.isEmpty(string)) {
                        column++;
                        continue;
                    }
                    try {
                        cell.setCellValue((Double) converterRegistry.convert(Double.class, string));
                    } catch (NumberFormatException e) {
                        cell.setCellValue(string.toString());
                    }
                } catch (IndexOutOfBoundsException e) {
                    // TODO: handle exception
                }
                column++;
                continue;
            }
            if (value.getClass().isEnum() && !tt.getEnumFieldName().equals("")) {
                str = ReflectUtil.invoke(value, ("get" + StrUtil.upperFirst(tt.getEnumFieldName())))
                        .toString();
                cell.setCellValue(str);
                column++;
                continue;
            }

            if (value.getClass().equals(Double.class) || value.getClass().equals(double.class)) {
                cell.setCellValue((Double) converterRegistry.convert(Double.class, value));
                column++;
                continue;
            }
            if (value.getClass().equals(Date.class) || value.getClass().equals(Timestamp.class)) {

                cell.setCellValue((Date) converterRegistry.convert(Date.class, value));

                column++;
                continue;
            }

            str = converterRegistry.convert(String.class, value);
            cell.setCellValue(str);

            column++;
        }

        rownum++;
    }

    @Override
    public void addAll(List<T> ts) {
        createSheet();
        for (T t : ts) {
            add(t);
            rownum++;
        }
    }

    @Override
    public void save(File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        sheet.getWorkbook().write(out);
        out.close();
    }

    @Override
    public void addEmptyLine() {
        if (rownum != 0)
            rownum++;
    }
}
