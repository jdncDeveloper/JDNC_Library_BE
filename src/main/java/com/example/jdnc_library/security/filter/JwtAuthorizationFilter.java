package com.example.jdnc_library.security.filter;


import static com.example.jdnc_library.security.filter.JwtAuthenticationFilter.AUTHORIZATION_HEADER;
import static com.example.jdnc_library.security.filter.JwtAuthenticationFilter.TOKEN_START_WITH;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.domain.member.repository.MemberRepository;
import com.example.jdnc_library.exception.clienterror._400.EntityNotFoundException;
import com.example.jdnc_library.exception.clienterror._401.UnauthorizedException;
import com.example.jdnc_library.security.model.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

// 시큐리티가 filter를 가지고 있는데 그 필터중에 BasicAuthenticationFilter 라는 것이 있음
// 권한이나 인증이 필요한 특정 주소를 요청했을때 위 필터를 무조건 타게 되어있음
// 만약에 권한이이나 인증이 필요한 주소가 아니라면 이 필터를 타지 않음
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final String emptyHeaderMessage = "Authorization Header is empty";
    private final String unsupportedHeader = "해당 jwt 형식을 지원하지 않습니다";
    private final String accessSecret;
    private final MemberRepository memberRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
        String accessSecret,
        MemberRepository memberRepository) {
        super(authenticationManager);
        this.accessSecret = accessSecret;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        String token = resolveTokenByHeader(request);
        try {
            Long id = parseIdByJwt(token);
            Member member = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Member.class));
            PrincipalDetails principalDetails = new PrincipalDetails(member);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                principalDetails, null, principalDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }catch (TokenExpiredException e) {
            response.sendError(401, e.getMessage());
        }
    }

    private Long parseIdByJwt(String token) {
        return JWT.require(Algorithm.HMAC512(accessSecret))
            .build()
            .verify(token)
            .getClaim("id").asLong();
    }

    private String resolveTokenByHeader (HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(bearerToken)) {
            throw new UnauthorizedException(emptyHeaderMessage);
        }

        if (!bearerToken.startsWith(TOKEN_START_WITH)){
            throw new UnauthorizedException(unsupportedHeader);
        }
        return bearerToken.substring(7);
    }
}
