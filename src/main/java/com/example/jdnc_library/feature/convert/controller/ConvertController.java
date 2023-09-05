package com.example.jdnc_library.feature.convert.controller;

import com.example.jdnc_library.domain.book.repository.BookRepository;
import com.example.jdnc_library.domain.book.repository.CollectionRepository;
import com.example.jdnc_library.feature.convert.service.ConvertToExelFileService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/excel")
public class ConvertController {

    private final ConvertToExelFileService convertToExelFileService;
    private final BookRepository bookRepository;
    private final CollectionRepository collectionRepository;

    @PostMapping
    @Operation(summary = "엑셀 다운로드", description = "특정 기간의 대여현황을 엑셀 파일로 변환하여 리턴")
    public ResponseEntity<ByteArrayResource> convertToExelFile(@RequestParam LocalDate start, @RequestParam LocalDate end)
        throws IOException {
        try {
            //엑셀 파일에 내용 세팅
            XSSFWorkbook workbook = convertToExelFileService.convertToExelFile(start, end);

            //파일 생성
            ByteArrayOutputStream outputStream = convertToExelFileService.makeExelFile(workbook);
            ByteArrayResource resource = convertToExelFileService.getResource(outputStream);

            //파일 이름 생성
            String fileName = convertToExelFileService.makeFileName(start, end);

            // 파일 다운로드 응답
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            headers.setContentDispositionFormData("attachment", fileName);
            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(outputStream.size())
                .body(resource);
        } catch (Exception e) {
            throw e;
        }
    }
}
