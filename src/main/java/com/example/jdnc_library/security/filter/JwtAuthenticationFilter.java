package com.example.jdnc_library.security.filter;

import static com.example.jdnc_library.security.jwt.TokenProvider.AUTHORIZATION_HEADER;
import static com.example.jdnc_library.security.jwt.TokenProvider.AUTHORIZATION_HEADER_REFRESH;
import static com.example.jdnc_library.security.jwt.TokenProvider.TOKEN_START_WITH;

import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.domain.member.repository.MemberRepository;
import com.example.jdnc_library.exception.clienterror._401.NotLoginException;
import com.example.jdnc_library.security.jwt.TokenProvider;
import com.example.jdnc_library.security.model.LoginInfo;
import com.example.jdnc_library.security.model.PrincipalDetails;
import com.example.jdnc_library.security.service.LmsCrawlerService;
import com.example.jdnc_library.util.RegexUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    private final AuthenticationManager authenticationManager;

    private final LmsCrawlerService lmsCrawlerService;

    private final TokenProvider tokenProvider;

    private final MemberRepository memberRepository;

    public JwtAuthenticationFilter(
        AuthenticationManager authenticationManager,
        ObjectMapper objectMapper,
        LmsCrawlerService lmsCrawlerService,
        TokenProvider tokenProvider,
        MemberRepository memberRepository) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.lmsCrawlerService = lmsCrawlerService;
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        try {
            LoginInfo loginInfo = objectMapper.readValue(request.getInputStream(),
                LoginInfo.class);

            if (isUsernameUserFormat(loginInfo.getUsername())) {
                authenticateUser(loginInfo, response);
            }
            
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginInfo.getUsername(),
                    loginInfo.getPassword());

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException("로그인 정보 파싱 실패");
        }
    }


    private void authenticateUser(LoginInfo lmsLoginInfo, HttpServletResponse response)
        throws IOException {
        try {
            lmsCrawlerService.getLmsLoginInfo(lmsLoginInfo);
        } catch (NotLoginException e) {
            response.sendError(401, e.getMessage());
        }
    }

    private boolean isUsernameUserFormat(String username) {
        return RegexUtil.isEightNumber(username);
    }

    private boolean isUsernameAdminFormat(String username) {
        return username.startsWith("admin");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        addAccessTokenInHeaders(principalDetails, response);
        String refresh = addRefreshTokenInHeaders(response);
        updateRefresh(principalDetails, refresh);
    }

    private void updateRefresh(PrincipalDetails principalDetails, String refresh) {
        principalDetails.getMember().updateRefresh(refresh);
        memberRepository.save(principalDetails.getMember());
    }

    private String addAccessTokenInHeaders(PrincipalDetails principalDetails, HttpServletResponse response) {
        String jwtToken = tokenProvider.createAccessToken(principalDetails.getMember());
        response.addHeader(AUTHORIZATION_HEADER, TOKEN_START_WITH + jwtToken);
        return jwtToken;
    }

    private String addRefreshTokenInHeaders(HttpServletResponse response) {
        String jwtToken = tokenProvider.createRefreshToken();
        response.addHeader(AUTHORIZATION_HEADER_REFRESH, TOKEN_START_WITH + jwtToken);
        return jwtToken;
    }

}
