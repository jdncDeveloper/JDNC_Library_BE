package com.example.jdnc_library.security.service;

import static com.example.jdnc_library.security.jwt.TokenProvider.AUTHORIZATION_HEADER;
import static com.example.jdnc_library.security.jwt.TokenProvider.AUTHORIZATION_HEADER_REFRESH;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.domain.member.repository.MemberRepository;
import com.example.jdnc_library.exception.clienterror._400.EntityNotFoundException;
import com.example.jdnc_library.exception.clienterror._400.TokenNotExpiredException;
import com.example.jdnc_library.exception.clienterror._401.NotLoginException;
import com.example.jdnc_library.security.jwt.TokenProvider;
import com.example.jdnc_library.security.jwt.exception.NotEqualRefreshTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshService {

    private final MemberRepository memberRepository;

    private final TokenProvider tokenProvider;

    public void reGenerateToken (HttpServletRequest request, HttpServletResponse response) {
        String jwt = tokenProvider.resolveAccessTokenByHeader(request);
        String refresh = tokenProvider.resolveRefreshTokenByHeader(request);
        if (jwt.isEmpty()) throw new NotLoginException("Authorization is Empty");
        if (refresh.isEmpty()) throw new NotLoginException("Authorization_Refresh is Empty");

        try {
            tokenProvider.parseIdByJwt(jwt);
            throw new TokenNotExpiredException();
        }catch (TokenExpiredException e) {

            try {
                Long id = JWT.decode(jwt).getClaim("id").asLong();
                Member member = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Member.class));

                if (!member.getRefresh().equals(refresh)) {
                    throw new NotEqualRefreshTokenException(String.valueOf(member.getId()), refresh);
                }

                String newRefresh = tokenProvider.createRefreshToken();
                member.updateRefresh(newRefresh);

                String newAccess = tokenProvider.createAccessToken(member);

                response.addHeader(AUTHORIZATION_HEADER, newAccess);
                response.addHeader(AUTHORIZATION_HEADER_REFRESH, newRefresh);

            } catch (EntityNotFoundException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new NotLoginException("jwt형식이 잘못되었습니다.");
            }
        }
    }

}
