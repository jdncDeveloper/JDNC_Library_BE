package com.example.jdnc_library.feature.convert.service;

import com.example.jdnc_library.feature.convert.entity.TestEntity;
import com.example.jdnc_library.feature.convert.repository.TestRentalListRepository;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
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

    private final TestRentalListRepository testRentalListRepository;

    public XSSFWorkbook ConvertToExelFile() throws IOException {
        //엑셀 파일을 가져옵니다
        String filePath = "src/main/resources/template.xlsm";

        FileInputStream fileInputStream = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

        //대여 현황 리스트를 가져옵니다
        List<TestEntity> values = testRentalListRepository.findAll();

        //대여 현황 입력
        Sheet sheet = workbook.getSheetAt(0);
        for(int i = 0; i < values.size(); i++) {
            Row row = sheet.getRow(4 + i);
            Cell cell1st = row.getCell(0);
            Cell cell2nd = row.getCell(1);
            Cell cell3rd = row.getCell(2);
            Cell cell4th = row.getCell(3);
            Cell cell5th = row.getCell(4);
            cell1st.setCellValue(values.get(i).getSerialNum());
            cell2nd.setCellValue(values.get(i).getBookName());
            cell3rd.setCellValue(values.get(i).getCardinalNum());
            cell4th.setCellValue(values.get(i).getUserName());
            cell5th.setCellValue(values.get(i).getRentalDateTime());
        }

        fileInputStream.close();

        return workbook;
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
