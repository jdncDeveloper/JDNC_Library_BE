package com.example.jdnc_library.init;

import com.example.jdnc_library.domain.book.model.BookGroup;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class BookInfoInitProvider {

    @Value("${init.excel.path}")
    private String excelPath;

    public List<BookInfoInitValue> getValueList() {
        List<BookInfoInitValue> list = new ArrayList<>();

        list.add(new BookInfoInitValue(BookGroup.GROUP_T, "TAM", 0));
        list.add(new BookInfoInitValue(BookGroup.GROUP_A, "TAM", 6));
        list.add(new BookInfoInitValue(BookGroup.GROUP_M, "TAM", 12));
        list.add(new BookInfoInitValue(BookGroup.GROUP_N, "na", 0));
        list.add(new BookInfoInitValue(BookGroup.GROUP_A2, "na", 6));
        return list;
    }

    @Data
    @AllArgsConstructor
    static class BookInfoInitValue {

        private BookGroup bookGroup;

        private String sheetName;

        private Integer beginRow = 2;

        private Integer endRow = 109;

        private Integer beginColumn;

        BookInfoInitValue(BookGroup bookGroup, String sheetName, Integer beginColumn) {
            this.bookGroup = bookGroup;
            this.sheetName = sheetName;
            this.beginColumn = beginColumn;
        }
    }
}