package com.example.jdnc_library.feature.aws.controller;

import com.example.jdnc_library.domain.ResponseData;
import com.example.jdnc_library.feature.aws.dto.FileRequest;
import com.example.jdnc_library.feature.aws.service.S3Service;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/image")
@RequiredArgsConstructor
public class ImageSaveController {

    private final S3Service s3Service;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseData<String> saveImage(@ModelAttribute FileRequest fileRequest) throws IOException {
        return new ResponseData<>(s3Service.uploadFile(fileRequest.getFile()));
    }
}
