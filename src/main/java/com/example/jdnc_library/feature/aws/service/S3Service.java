package com.example.jdnc_library.feature.aws.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.jdnc_library.exception.clienterror._400.BadRequestException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp");

    public String uploadFile(MultipartFile file) throws IOException {

        if (file == null) {
            throw new BadRequestException("파일이 없습니다.");
        }

        validationExtension(file);

        String fileName = file.getOriginalFilename() + UUID.randomUUID();
        ObjectMetadata metadata= new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, fileName,file.getInputStream(),metadata);

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private boolean validationExtension(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // Check if the file extension is in the list of allowed image extensions
            return ALLOWED_IMAGE_EXTENSIONS.contains(fileExtension.toLowerCase());
        }catch (NullPointerException e) {
            throw new BadRequestException("파일명이 존재하지 않습니다.");
        }
    }

}
