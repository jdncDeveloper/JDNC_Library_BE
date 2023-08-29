package com.example.jdnc_library.feature.naverAPI.controller;

import com.example.jdnc_library.feature.naverAPI.DTO.ExtractionBookInfoDTO;
import com.example.jdnc_library.feature.naverAPI.DTO.NaverAPIDTO;
import com.example.jdnc_library.feature.naverAPI.service.NaverAPIService;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NaverAPIController {

    private final NaverAPIService naverAPIService;

    @GetMapping("/api/naver-books")
    @ResponseStatus(HttpStatus.OK)
    public List<ExtractionBookInfoDTO> naverAPI(@RequestParam String title, @RequestParam Integer page) throws URISyntaxException {
        List<ExtractionBookInfoDTO> BookInfoList = naverAPIService.GetBookInfoList(title, page);

        return BookInfoList;
    }
}
