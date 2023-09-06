package com.example.jdnc_library.feature.convert.service;

import com.example.jdnc_library.domain.book.model.BookGroup;
import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.domain.book.model.CollectionInfo;
import com.example.jdnc_library.domain.book.repository.BorrowRepository;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
import com.example.jdnc_library.exception.clienterror._400.BadRequestException;
import jakarta.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConvertToExelFileService {

    private final BorrowRepository borrowRepository;
    private final CollectionRepository collectionRepository;
    @Transactional
    public XSSFWorkbook convertToExelFile(int year, int month) throws IOException {

        Cell[] cells = new Cell[7];

        try {
            //엑셀 파일을 가져옵니다
            String filePath = "src/main/resources/template.xlsm";

            FileInputStream fileInputStream = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

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

            fileInputStream.close();

            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    public ByteArrayOutputStream makeExelFile(XSSFWorkbook workbook) throws IOException {
        //파일 생성
        try (ByteArrayOutputStream fileOut = new ByteArrayOutputStream()) {
            workbook.write(fileOut);
            return fileOut;
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            workbook.close();
        }
    }

    public String makeFileName(int year, int month) {
        //파일이름 생성
        String fileName = "";
        fileName = year + "_" + month + "_LIB.xlsm";
        System.out.println(fileName);

        return fileName;
    }

    public ByteArrayResource getResource(ByteArrayOutputStream outputStream) {
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        return resource;
    }
}
