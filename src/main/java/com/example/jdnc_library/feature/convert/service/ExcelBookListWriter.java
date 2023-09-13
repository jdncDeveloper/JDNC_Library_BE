package com.example.jdnc_library.feature.convert.service;

import com.example.jdnc_library.domain.book.model.BookGroup;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
import com.example.jdnc_library.exception.clienterror._400.BadRequestException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExcelBookListWriter {

    private final CollectionRepository collectionRepository;

    public XSSFWorkbook inputBookList(XSSFWorkbook workbook) {
        Cell[] cells = new Cell[2];

        //도서 리스트를 가져옵니다
        List<CollectionInfo> collections = collectionRepository.findAll();

        //엑셀에 도서 리스트 입력
        int[] rowCount = new int[6];
        int index = 0;

        Sheet lostSheet = workbook.getSheetAt(7);
        Row lostRow = lostSheet.getRow(4 + rowCount[5]);

        for (int i = 0; i < collections.size(); i++) {
            BookGroup bookGroup = collections.get(i).getBookInfo().getBookGroup();
            Sheet nowSheet;
            switch (bookGroup) {
                case GROUP_T:
                    nowSheet = workbook.getSheetAt(1);
                    index = 0;
                    break;
                case GROUP_A:
                    nowSheet = workbook.getSheetAt(2);
                    index = 1;
                    break;
                case GROUP_M:
                    nowSheet = workbook.getSheetAt(3);
                    index = 2;
                    break;
                case GROUP_N:
                    nowSheet = workbook.getSheetAt(4);
                    index = 3;
                    break;
                case GROUP_A2:
                    nowSheet = workbook.getSheetAt(5);
                    index = 4;
                    break;
                default:
                    throw new BadRequestException("도서 그룹에 맞는 시트가 없습니다");
            }

            Row row = nowSheet.getRow(2 + rowCount[index]);

            rowCount[index]++;
            cells[0] = row.getCell(0);
            cells[1] = row.getCell(1);
            cells[0].setCellValue(collections.get(i).getBookNumber());
            cells[1].setCellValue(collections.get(i).getBookInfo().getTitle());
            if (collections.get(i).isLost()) { //소실된 책 처리
                rowCount[5]++;
                cells[0] = lostRow.getCell(0);
                cells[1] = lostRow.getCell(1);
                cells[0].setCellValue(collections.get(i).getBookNumber());
                cells[1].setCellValue(collections.get(i).getBookInfo().getTitle());
            }
        }

        return workbook;
    }
}
