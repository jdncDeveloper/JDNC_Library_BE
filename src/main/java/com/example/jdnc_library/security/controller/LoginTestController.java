package com.example.jdnc_library.security.controller;

import com.example.jdnc_library.security.model.LmsTotalInfo;
import com.example.jdnc_library.security.model.PrincipalDetails;
import com.example.jdnc_library.security.service.LmsCrawlerService;
import com.example.jdnc_library.security.model.LmsLoginInfo;
import com.example.jdnc_library.security.service.LmsService;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class LoginTestController {

    private final LmsService lmsService;

    private final LmsCrawlerService lmsCrawlerService;

    @PostMapping
    public String getName (@RequestBody LmsLoginInfo lmsLoginInfo)
        throws IOException, NoSuchAlgorithmException, KeyManagementException {
        return lmsService.getNameWithLogin(lmsLoginInfo);
    }

    @PostMapping("2")
    public LmsTotalInfo getUserInfo (@RequestBody LmsLoginInfo lmsLoginInfo)
        throws IOException, NoSuchAlgorithmException, KeyManagementException {
        return lmsCrawlerService.getLmsLoginInfo(lmsLoginInfo);
    }

    @GetMapping("1")
    @PreAuthorize("isAuthenticated()")
    public String getMyName (@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return principalDetails.getMember().getName();
    }

}
