package com.example.jdnc_library.security.filter;

import static com.example.jdnc_library.security.jwt.TokenProvider.AUTHORIZATION_HEADER;
import static com.example.jdnc_library.security.jwt.TokenProvider.AUTHORIZATION_HEADER_REFRESH;
import static com.example.jdnc_library.security.jwt.TokenProvider.TOKEN_START_WITH;

import com.example.jdnc_library.domain.member.repository.MemberRepository;
import com.example.jdnc_library.exception.clienterror._401.NotLoginException;
import com.example.jdnc_library.security.jwt.TokenProvider;
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
            LmsLoginInfo lmsLoginInfo = objectMapper.readValue(request.getInputStream(),
                LmsLoginInfo.class);
            try {
                LmsTotalInfo lmsTotalInfo = lmsCrawlerService.getLmsLoginInfo(lmsLoginInfo);

            } catch (NotLoginException e) {
                response.sendError(401, e.getMessage());
                return null;
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(lmsLoginInfo.getUsername(),
                    lmsLoginInfo.getPassword());

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
