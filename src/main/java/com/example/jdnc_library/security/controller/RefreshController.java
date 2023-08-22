package com.example.jdnc_library.security.controller;

import com.example.jdnc_library.security.jwt.TokenProvider;
import com.example.jdnc_library.security.service.RefreshService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/refresh")
@RequiredArgsConstructor
public class RefreshController {

    private final RefreshService refreshService;

    @PostMapping
    public void tokenRefresh(HttpServletRequest request, HttpServletResponse response) {
        refreshService.reGenerateToken(request, response);
    }
}
