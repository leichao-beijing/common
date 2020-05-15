package run.cmid.common.excel.plugins;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class StyleInfoUtils {

    private HashMap<String, CellStyle> map = new HashMap<>();

    public static StyleInfo createInfo() {
        return new StyleInfo();
    }

    public CellStyle buildStyle(Workbook workbook, StyleInfo info) {
        CellStyle style = map.get(info.toString());
        if (style == null)
            return createStyle(workbook, info);
        return style;
    }

    private CellStyle createStyle(Workbook workbook, StyleInfo info) {
        return null;
    }
}
