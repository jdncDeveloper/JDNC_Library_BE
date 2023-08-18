package com.example.jdnc_library.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jdnc_library.exception.clienterror._401.NotLoginException;
import com.example.jdnc_library.security.model.LmsLoginInfo;
import com.example.jdnc_library.security.model.LmsTotalInfo;
import com.example.jdnc_library.security.model.PrincipalDetails;
import com.example.jdnc_library.security.service.LmsCrawlerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_START_WITH = "Bearer ";
    private final ObjectMapper objectMapper;
    private AuthenticationManager authenticationManager;
    private final String accessSecret;
    private final LmsCrawlerService lmsCrawlerService;

    public JwtAuthenticationFilter (
        AuthenticationManager authenticationManager,
        ObjectMapper objectMapper,
        String access,
        LmsCrawlerService lmsCrawlerService) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.accessSecret = access;
        this.lmsCrawlerService = lmsCrawlerService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        try {
            LmsLoginInfo lmsLoginInfo = objectMapper.readValue(request.getInputStream(), LmsLoginInfo.class);
            try {
                LmsTotalInfo lmsTotalInfo  = lmsCrawlerService.getLmsLoginInfo(lmsLoginInfo);

            }catch (NotLoginException e) {
                response.sendError(401, e.getMessage());
                return null;
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(lmsLoginInfo.getUsername(), lmsLoginInfo.getPassword());

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException("로그인 정보 파싱 실패");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = createAccessToken(principalDetails);

        response.addHeader("Authorization", TOKEN_START_WITH + jwtToken);
    }

    private String createAccessToken(PrincipalDetails principalDetails) {
        return JWT.create()
            .withSubject("jdnc_library_access_token")
            .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))
            .withClaim("id", principalDetails.getMember().getId())
            .sign(Algorithm.HMAC512(accessSecret));
    }
}
