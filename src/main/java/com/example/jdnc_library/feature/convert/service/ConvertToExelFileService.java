package com.example.jdnc_library.feature.convert.service;

import com.example.jdnc_library.exception.clienterror._400.BadRequestException;
import jakarta.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConvertToExelFileService {
    
    private final ExcelBookListWriter excelBookListWriter;
    private final ExcelBorrowListWriter excelBorrowListWriter;

    @Transactional
    public XSSFWorkbook convertToExelFile(int year, int month) throws IOException {

        try {
            //엑셀 파일을 가져옵니다
            ClassPathResource resource = new ClassPathResource("template.xlsm");

            InputStream fileInput = resource.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(fileInput);

            fileInput.close();

            //도서 리스트 입력
            workbook = excelBookListWriter.inputBookList(workbook);

            //대여 현황 입력
            workbook = excelBorrowListWriter.inputBorrowList(workbook, year, month);

            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    public ByteArrayOutputStream convertToByteArrayOutputStream(XSSFWorkbook workbook) throws IOException {
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
