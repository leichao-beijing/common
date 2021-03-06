package run.cmdi.common.reader.model.to;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.reader.annotations.ConverterHead;

/**
 * @author leichao
 */
@Getter
@Setter
public class ExcelHeadModel {

    public ExcelHeadModel() {
    }

    public ExcelHeadModel(ConverterHead head) {
        if (head == null)
            return;
        maxWrongCount = head.maxWrongCount();
        skipNoAnnotationField = head.skipNoAnnotationField();
        bookTagName = head.bookTagName();
    }

    private int maxWrongCount = 3;

    /**
     * 开始后跳过没有ExcelConverter的Field字段。默认false
     */
    private boolean skipNoAnnotationField = false;
    private String bookTagName;

}
