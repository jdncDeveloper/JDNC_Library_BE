package com.example.jdnc_library.init;

import com.example.jdnc_library.domain.book.model.BookInfo;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.domain.book.repository.BookRepository;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
import com.example.jdnc_library.init.InitBookInfoProvider.InitBookInfoValue;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class BookInfoInitializer {

    private final BookRepository bookRepository;

    private final CollectionRepository collectionRepository;

    private final InitBookInfoProvider initProvider;

    @PostConstruct
    @Transactional(rollbackFor = RuntimeException.class)
    public void init() throws IOException {
        if (collectionRepository.count() > 0) return;
        XSSFWorkbook workbook = getXSSFWorkBook();
        parseExcelToCollectionInfo(workbook);
    }

    private XSSFWorkbook getXSSFWorkBook() throws IOException {
        ClassPathResource resource = new ClassPathResource("excel/baseinfo.xlsx");
        return new XSSFWorkbook(resource.getInputStream());
    }

    private void parseExcelToCollectionInfo (XSSFWorkbook workbook) {
        List<InitBookInfoValue> infoValues = initProvider.getValueList();

        for (InitBookInfoValue initValue : infoValues) {
            //시트 추출
            XSSFSheet sheet = workbook.getSheet(initValue.getSheetName());
            for (int i = initValue.getBeginRow(); i <= initValue.getEndRow(); i++) {
                //해당 시트로 부터 개별 row 추출
                XSSFRow row = sheet.getRow(i);

                //row로 부터 책정보 및 소장정보 추출
                CollectionInfo collectionInfo = parseExcelToCollectionInfos(row, initValue);

                if (collectionInfo == null) break;

                collectionRepository.save(collectionInfo);
            }
        }

    }

    private CollectionInfo parseExcelToCollectionInfos(XSSFRow row, InitBookInfoValue infoValue) {

        BookInfo bookInfo = parseExcelToBookInfo(row, infoValue);
        if (bookInfo == null) {
            return null;
        }

        Long bookNumber = (long) row.getCell(infoValue.getBeginColumn()).getNumericCellValue();

        return new CollectionInfo(bookInfo, bookNumber);

    }

    private BookInfo parseExcelToBookInfo(XSSFRow row, InitBookInfoValue infoValue) {
        String title = row.getCell(infoValue.getBeginColumn() + 1).getStringCellValue();

        if (!StringUtils.hasText(title)) {
            return null;
        }
        //orElse 주의
        return bookRepository.findByTitle(title)
            .orElseGet(() -> bookRepository.save(new BookInfo(title, infoValue.getBookGroup())));
    }
}
