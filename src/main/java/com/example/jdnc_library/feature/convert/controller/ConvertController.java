package com.example.jdnc_library.feature.convert.controller;

import com.example.jdnc_library.feature.convert.service.ConvertToExelFileService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/management")
public class ConvertController {

    private final ConvertToExelFileService convertToExelFileService;

    @PostMapping("/excel")
    public ResponseEntity<ByteArrayResource> ConvertToExelFile()
        throws IOException {

        //엑셀 파일에 내용 세팅
        XSSFWorkbook workbook = convertToExelFileService.ConvertToExelFile();

        //파일 생성
        ByteArrayOutputStream outputStream = convertToExelFileService.makeExelFile(workbook);
        ByteArrayResource resource = convertToExelFileService.getResource(outputStream);

        //파일 이름 생성
        String fileName = convertToExelFileService.makeFileName();

        // 파일 다운로드 응답
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        headers.setContentDispositionFormData("attachment", fileName);
        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(outputStream.size())
            .body(resource);
    }
}
