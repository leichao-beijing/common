package run.cmid.common.reader.model.to;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.reader.model.FieldDetail;

/**
 * 
 * @author leichao
 */
@Getter
@Setter
public class FindSheetModel<T> {
    public FindSheetModel(List<FieldDetail> findHeadField) {
        this.findHeadField = findHeadField;
    }

    private Sheet findSheet;
    private final List<FieldDetail> findHeadField;

}
