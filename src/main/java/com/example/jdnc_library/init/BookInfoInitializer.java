package com.example.jdnc_library.init;

import com.example.jdnc_library.domain.book.model.BookGroup;
import com.example.jdnc_library.domain.book.model.BookInfo;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.domain.book.repository.BookRepository;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
import com.example.jdnc_library.init.InitBookInfoProvider.InitBookInfoValue;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
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
        if (bookRepository.count() > 0) {
            return;
        }
        if (collectionRepository.count() > 0) {
            return;
        }
        XSSFWorkbook workbook = getXSSFWorkBook();
        saveExcelData(workbook);
        workbook.close();
    }

    /**
     * 엑셀 데이터 가져오기
     *
     * @return
     * @throws IOException
     */
    private XSSFWorkbook getXSSFWorkBook() throws IOException {
        ClassPathResource resource = new ClassPathResource("excel/baseinfo.xlsx");
        InputStream inputStream = resource.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(resource.getInputStream());
        inputStream.close();
        return workbook;
    }

    /**
     * 엑셀 데이터를 기반으로 데이터베이스 저장
     *
     * @param workbook
     */
    private void saveExcelData(XSSFWorkbook workbook) {
        List<InitBookInfoValue> infoValues = initProvider.getValueList();

        for (InitBookInfoValue initValue : infoValues) {
            //시트 추출
            XSSFSheet sheet = workbook.getSheet(initValue.getSheetName());

            String beforeTitle = "";
            BookInfo beforeBookInfo = null;
            for (int i = initValue.getBeginRow(); i <= initValue.getEndRow(); i++) {
                //해당 시트로 부터 개별 row 추출
                XSSFRow row = sheet.getRow(i);

                //row로부터 title 추출
                String title = parseTitleByExcel(row, initValue);

                //제목이 없을경우 작업 중지
                if (!StringUtils.hasText(title)) {
                    break;
                }

                BookInfo bookInfo;

                //title이 같을경우 이전 엔티티 참조
                if (beforeTitle.equals(title)) {
                    bookInfo = beforeBookInfo;
                } else {
                    bookInfo = getBookInfo(title, initValue.getBookGroup());
                }

                if (bookInfo == null) {
                    break;
                }

                //새로운 소장정보 생성
                CollectionInfo collectionInfo = createCollectionInfoByExcelRow(row, bookInfo,
                    initValue);

                //소장정보 저장
                collectionRepository.save(collectionInfo);

                beforeBookInfo = bookInfo;
            }
        }
    }

    private CollectionInfo createCollectionInfoByExcelRow(XSSFRow row, BookInfo bookInfo,
        InitBookInfoValue infoValue) {
        Long bookNumber = getBookNumberByRow(row, infoValue);
        return new CollectionInfo(bookInfo, bookNumber);
    }


    private Long getBookNumberByRow(XSSFRow row, InitBookInfoValue infoValue) {
        return (long) row.getCell(infoValue.getBeginColumn()).getNumericCellValue();
    }

    private BookInfo getBookInfo(String title, BookGroup bookGroup) {
        if (!StringUtils.hasText(title)) {
            return null;
        }

        //orElse 주의
        return bookRepository.findByTitle(title)
            .orElseGet(() -> bookRepository.save(new BookInfo(title, bookGroup)));
    }

    private String parseTitleByExcel(XSSFRow row, InitBookInfoValue infoValue) {
        return row.getCell(infoValue.getBeginColumn() + 1).getStringCellValue();
    }
}
