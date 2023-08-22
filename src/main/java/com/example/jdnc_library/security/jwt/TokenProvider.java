package com.example.jdnc_library.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.exception.clienterror._401.UnauthorizedException;
import com.example.jdnc_library.security.model.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TokenProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_HEADER_REFRESH = "Authorization-Refresh";
    public static final String TOKEN_START_WITH = "Bearer ";
    private final String accessSecret;
    private final String refreshSecret;
    private final String emptyHeaderMessage = "Authorization Header is empty";
    private final String unsupportedHeader = "해당 jwt 형식을 지원하지 않습니다";

    public TokenProvider (
        @Value("${jwt.access}") String access,
        @Value("${jwt.refresh}") String refresh) {
        this.accessSecret = access;
        this.refreshSecret = refresh;
    }

    public String createAccessToken(Member member) {
        return JWT.create()
            .withSubject("jdnc_library_access_token")
            .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 60 * 3)))
            .withClaim("id", member.getId())
            .sign(Algorithm.HMAC512(accessSecret));
    }

    public String createRefreshToken() {
        return JWT.create()
            .withSubject("jdnc_library_refresh_token")
            .withClaim("random_seed", String.valueOf(Math.random()))
            .withExpiresAt(new Date(System.currentTimeMillis() + (6000000)))
            .sign(Algorithm.HMAC512(refreshSecret));
    }

    public Long parseIdByJwt(String token) {
        return JWT.require(Algorithm.HMAC512(accessSecret))
            .build()
            .verify(token)
            .getClaim("id").asLong();
    }

    public String resolveAccessTokenByHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(bearerToken)) {
            throw new UnauthorizedException(emptyHeaderMessage);
        }

        if (!bearerToken.startsWith(TOKEN_START_WITH)){
            throw new UnauthorizedException(unsupportedHeader);
        }
        return bearerToken.substring(7);
    }

    public String resolveRefreshTokenByHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER_REFRESH);

        if (!StringUtils.hasText(bearerToken)) {
            throw new UnauthorizedException(emptyHeaderMessage);
        }

        if (!bearerToken.startsWith(TOKEN_START_WITH)){
            throw new UnauthorizedException(unsupportedHeader);
        }
        return bearerToken.substring(7);
    }
}
