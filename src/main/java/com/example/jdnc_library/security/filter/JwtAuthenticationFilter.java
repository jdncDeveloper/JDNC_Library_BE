package com.example.jdnc_library.security.filter;

import static com.example.jdnc_library.security.jwt.TokenProvider.AUTHORIZATION_HEADER;
import static com.example.jdnc_library.security.jwt.TokenProvider.AUTHORIZATION_HEADER_REFRESH;
import static com.example.jdnc_library.security.jwt.TokenProvider.TOKEN_START_WITH;

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
            return processingLogin(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Authentication processingLogin(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        try {
            LoginInfo loginInfo = objectMapper.readValue(request.getInputStream(),
                LoginInfo.class);

            if (isUsernameUserFormat(loginInfo.getUsername())) {
                authenticateUser(loginInfo);
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginInfo.getUsername(),
                    loginInfo.getPassword());

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            response.sendError(400, "로그인 정보의 형태가 맞지 않습니다.");
        } catch (NotLoginException e) {
            response.sendError(401, e.getMessage());
        }

        return null;
    }


    private void authenticateUser(LoginInfo lmsLoginInfo) throws IOException {
        lmsCrawlerService.getLmsLoginInfo(lmsLoginInfo);
    }

    private boolean isUsernameUserFormat(String username) {
        return RegexUtil.isEightNumber(username);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        //헤더 삽입
        addAccessTokenInHeaders(principalDetails, response);
        String refresh = addRefreshTokenInHeaders(response);

        //DB저장
        updateRefresh(principalDetails, refresh);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {
        response.sendError(401, "로그인 정보가 맞지 않습니다.");
    }

    private void updateRefresh(PrincipalDetails principalDetails, String refresh) {
        principalDetails.getMember().updateRefresh(refresh);
        memberRepository.save(principalDetails.getMember());
    }

    private String addAccessTokenInHeaders(PrincipalDetails principalDetails,
        HttpServletResponse response) {
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
