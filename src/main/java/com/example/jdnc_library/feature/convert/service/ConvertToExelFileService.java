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
    public XSSFWorkbook ConvertToExelFile(LocalDate start, LocalDate end) throws IOException {
        try {
            //엑셀 파일을 가져옵니다
            String filePath = "src/main/resources/template.xlsm";

            FileInputStream fileInputStream = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

            //도서 리스트를 가져옵니다
            List<CollectionInfo> collections = collectionRepository.findAll();

            //도서 리스트 입력
            int[] rowCount = new int[5];
            int index = 0;
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
                Cell cellBookNum = row.getCell(0);
                Cell cellBookTitle = row.getCell(1);
                cellBookNum.setCellValue(collections.get(i).getBookNumber());
                cellBookTitle.setCellValue(collections.get(i).getBookInfo().getTitle());
                if (collections.get(i).isLost()) {
                    Cell cellExtra = row.getCell(5);
                    cellExtra.setCellValue("소실");
                }
            }

            //대여 현황 리스트를 가져옵니다
            LocalDateTime startDate = start.atStartOfDay();
            LocalDateTime endDate = end.atTime(23, 59, 59);
            List<BorrowInfo> borrowInfoList = borrowRepository.findAllByCreatedAtBetween(startDate,
                endDate);

            //대여 현황 입력
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < borrowInfoList.size(); i++) {
                Row row = sheet.getRow(4 + i);
                Cell cell1st = row.getCell(0);
                Cell cell2nd = row.getCell(1);
                Cell cell3rd = row.getCell(2);
                Cell cell4th = row.getCell(3);
                Cell cell5th = row.getCell(4);
                cell1st.setCellValue(borrowInfoList.get(i).getCollectionInfo().getBookNumber());
                cell2nd.setCellValue(
                    borrowInfoList.get(i).getCollectionInfo().getBookInfo().getTitle());
                cell3rd.setCellValue(borrowInfoList.get(i).getCreatedBy().getUsername());
                cell4th.setCellValue(borrowInfoList.get(i).getCreatedBy().getName());
                cell5th.setCellValue(borrowInfoList.get(i).getCreatedAt());
            }

            fileInputStream.close();

            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
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

    public String makeFileName() {
        //파일이름 생성
        String fileName = "";
        LocalDate localDate = LocalDate.now();
        fileName = localDate + "_TAM-NA-LIB.xlsm";
        System.out.println(fileName);

        return fileName;
    }

    public ByteArrayResource getResource(ByteArrayOutputStream outputStream) {
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        return resource;
    }
}
