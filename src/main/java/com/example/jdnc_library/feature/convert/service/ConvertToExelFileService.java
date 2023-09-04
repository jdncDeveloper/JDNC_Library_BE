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
    public XSSFWorkbook convertToExelFile(LocalDate start, LocalDate end) throws IOException {
        try {
            //엑셀 파일을 가져옵니다
            String filePath = "src/main/resources/template.xlsm";

            FileInputStream fileInputStream = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

            //도서 리스트를 가져옵니다
            List<CollectionInfo> collections = collectionRepository.findAll();

            //도서 리스트 입력
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
                Cell cellBookNum = row.getCell(0);
                Cell cellBookTitle = row.getCell(1);
                cellBookNum.setCellValue(collections.get(i).getBookNumber());
                cellBookTitle.setCellValue(collections.get(i).getBookInfo().getTitle());
                if (collections.get(i).isLost()) { //소실된 책 처리
                    rowCount[5]++;
                    cellBookNum = lostRow.getCell(0);
                    cellBookTitle = lostRow.getCell(1);
                    cellBookNum.setCellValue(collections.get(i).getBookNumber());
                    cellBookTitle.setCellValue(collections.get(i).getBookInfo().getTitle());
                }
            }

            //대여 현황 리스트를 가져옵니다
            LocalDateTime startDate = start.atStartOfDay();
            LocalDateTime endDate = end.atTime(23, 59, 59);
            List<BorrowInfo> borrowInfoList = borrowRepository.findAllByCreatedAtBetween(startDate,
                endDate);

            //대여 현황 입력
            Sheet current = workbook.getSheetAt(0);
            Sheet record = workbook.getSheetAt(6);
            Row currentRow;
            Row recordRow;
            int currentIndex = 4;
            int recordIndex = 4;
            Cell cell1st;
            Cell cell2nd;
            Cell cell3rd;
            Cell cell4th;
            Cell cell5th;
            for (int i = 0; i < borrowInfoList.size(); i++) {
                if(borrowInfoList.get(i).isAdminCheck()) { // 반납 최종 확인 된 경우
                    recordRow = record.getRow(recordIndex);

                    cell1st = recordRow.getCell(0);
                    cell2nd = recordRow.getCell(1);
                    cell3rd = recordRow.getCell(2);
                    cell4th = recordRow.getCell(3);
                    cell5th = recordRow.getCell(4);
                    Cell cell6th = recordRow.getCell(5);
                    Cell cell7th = recordRow.getCell(6);

                    cell6th.setCellValue(borrowInfoList.get(i).getReturnDate());

                    recordIndex++;
                } else { //아직 반납 최정 확인아 안된경우
                    currentRow = current.getRow(currentIndex);

                    cell1st = currentRow.getCell(0);
                    cell2nd = currentRow.getCell(1);
                    cell3rd = currentRow.getCell(2);
                    cell4th = currentRow.getCell(3);
                    cell5th = currentRow.getCell(4);

                    currentIndex++;
                }

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

    public String makeFileName(LocalDate start, LocalDate end) {
        //파일이름 생성
        String fileName = "";
        fileName = start + " ~ " + end + "_LIB.xlsm";
        System.out.println(fileName);

        return fileName;
    }

    public ByteArrayResource getResource(ByteArrayOutputStream outputStream) {
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        return resource;
    }
}
