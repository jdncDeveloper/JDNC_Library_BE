package com.example.jdnc_library.security.model;


import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.exception.clienterror._401.NotLoginException;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// Security Session => Authentication => UserDetails
// 세션안에 들어갈수있는건 어쎈티케이션 객체고 그안에서 유저정보를 저장하는건 UserDetails 객체
//Authentication 객체는 PrincipalDetailsService 에서 만듬
@Data
public class PrincipalDetails implements UserDetails { //이렇게 하면 Authentication 객체안에 넣을수잇음

    private Member member;

    //JWT 필터에 사용되는 생성자
    public PrincipalDetails(Member member) {
        this.member = member;
    }

    //해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(member.getRole());
        return collect;
    }

    public Member getMember() throws NotLoginException {
        if (member == null) {
            throw new NotLoginException("유저 정보가 없습니다.");
        }

        return member;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


