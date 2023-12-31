package com.example.jdnc_library.feature.convert.controller;

import com.example.jdnc_library.domain.book.repository.BookRepository;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
import com.example.jdnc_library.feature.convert.service.ConvertToExelFileService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/excel")
public class ConvertController {

    private final ConvertToExelFileService convertToExelFileService;

    @GetMapping
    @Operation(summary = "엑셀 다운로드", description = "특정 기간의 대여현황을 엑셀 파일로 변환하여 리턴")
    public ResponseEntity<ByteArrayResource> convertToExelFile(@RequestParam int year, @RequestParam int month)
        throws IOException {
        XSSFWorkbook workbook = null;
        ByteArrayOutputStream outputStream = null;

        try {
            //엑셀 파일에 내용 세팅
            workbook = convertToExelFileService.convertToExelFile(year, month);

            //파일 생성
            outputStream = convertToExelFileService.convertToByteArrayOutputStream(workbook);
            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

            //파일 이름 생성
            String fileName = convertToExelFileService.makeFileName(year, month);

            // 파일 다운로드 응답
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            headers.setContentDispositionFormData("attachment", fileName);

            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(outputStream.size())
                .body(resource);

        } finally {
            if (workbook != null) workbook.close();
            if (outputStream != null) outputStream.close();
        }
    }
}
