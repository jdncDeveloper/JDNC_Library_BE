package com.example.jdnc_library.feature.naverAPI.service;

import com.example.jdnc_library.exception.clienterror._400.BadRequestException;
import com.example.jdnc_library.feature.naverAPI.DTO.ExtractionBookInfoDTO;
import com.example.jdnc_library.feature.naverAPI.DTO.NaverAPIDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NaverAPIService {
    @Value("${naver.client.id}")
    private String clientId;
    @Value("${naver.client.secret}")
    private String clientSecret;

    public List<ExtractionBookInfoDTO> GetBookInfoList(String title, int page) throws URISyntaxException {
        String encodedTitle;
        try {
            encodedTitle = URLEncoder.encode(title, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException();
        }

        int start = (page - 1) * 10 + 1;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        URI url = new URI("https://openapi.naver.com/v1/search/book.json?query=" + encodedTitle + "&start=" + start);

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, url);

        ResponseEntity<NaverAPIDTO> responseEntity = restTemplate.exchange(requestEntity, NaverAPIDTO.class);

        NaverAPIDTO naverAPIDTO = responseEntity.getBody();

        List<ExtractionBookInfoDTO> bookInfoDTOList = new ArrayList<>();
        for(NaverAPIDTO.BookDTO book : naverAPIDTO.getItems()) {
            ExtractionBookInfoDTO newBook = new ExtractionBookInfoDTO();
            newBook.setTitle(book.getTitle());
            newBook.setImage(book.getImage());
            newBook.setAuthor(book.getAuthor());
            newBook.setPublisher(book.getPublisher());
            newBook.setDescription(book.getDescription());

            bookInfoDTOList.add(newBook);
        }

        return bookInfoDTOList;
    }
}
