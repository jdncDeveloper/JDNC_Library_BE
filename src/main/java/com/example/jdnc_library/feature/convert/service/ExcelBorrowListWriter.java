package com.example.jdnc_library.feature.convert.service;

import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.domain.book.repository.BorrowRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExcelBorrowListWriter {

    private final BorrowRepository borrowRepository;

    public XSSFWorkbook inputBorrowList(XSSFWorkbook workbook, int year, int month) {
        Cell[] cells = new Cell[7];

        //대여 현황 리스트를 가져옵니다
        Month currentMonth = Month.of(month);
        YearMonth yearMonth = YearMonth.of(year, currentMonth);
        LocalDateTime startDate = LocalDateTime.of(year, currentMonth, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, currentMonth, yearMonth.lengthOfMonth(), 23, 59, 59);
        List<BorrowInfo> borrowInfoList = borrowRepository.findAllByCreatedAtBetween(startDate,
            endDate);

        //엑셀에 대여 현황 입력
        Sheet current = workbook.getSheetAt(0);
        Sheet record = workbook.getSheetAt(6);
        Row currentRow;
        Row recordRow;
        int currentIndex = 4;
        int recordIndex = 4;
        for (int i = 0; i < borrowInfoList.size(); i++) {
            if(borrowInfoList.get(i).isAdminCheck()) { // 반납 최종 확인 된 경우
                recordRow = record.getRow(recordIndex);

                for(int j = 0; j < cells.length; j++) {
                    cells[j] = recordRow.getCell(j);
                }

                cells[5].setCellValue(borrowInfoList.get(i).getReturnDate());
                cells[6].setCellValue(borrowInfoList.get(i).getFloor());

                recordIndex++;
            } else { // 반납 최종 확인이 안된경우
                currentRow = current.getRow(currentIndex);

                for(int j = 0; j < 5; j++) {
                    cells[j] = currentRow.getCell(j);
                }

                currentIndex++;
            }

            cells[0].setCellValue(borrowInfoList.get(i).getCollectionInfo().getBookNumber());
            cells[1].setCellValue(
                borrowInfoList.get(i).getCollectionInfo().getBookInfo().getTitle());
            cells[2].setCellValue(borrowInfoList.get(i).getCreatedBy().getUsername());
            cells[3].setCellValue(borrowInfoList.get(i).getCreatedBy().getName());
            cells[4].setCellValue(borrowInfoList.get(i).getCreatedAt());
        }

        return workbook;
    }
}
