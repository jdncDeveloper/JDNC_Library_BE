package com.example.jdnc_library.feature.member.controller;

import com.example.jdnc_library.domain.member.model.Role;
import com.example.jdnc_library.feature.manager.model.StatusNResultDTO;
import com.example.jdnc_library.feature.member.model.MemberDTO;
import com.example.jdnc_library.feature.member.service.MemberService;
import com.example.jdnc_library.security.model.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 로그인한 유저의 정보를 리턴
     * 인재번호, 이름, 이메일, 권한
     */
    @GetMapping({"", "/"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MemberDTO> getMember() {
        MemberDTO memberDTO = memberService.getMember();

        return ResponseEntity.ok(memberDTO);
    }

    /**
     * 로그인한 유저의 role을 리턴
     */
    @GetMapping("/role")
    @ResponseStatus(HttpStatus.OK)
    public String getRole() {
        Role role = memberService.getRole();

        return role.toString();
    }

    /**
     * 모든 유저 리스트 불러오기(페이징)
     * @param "page", "size"
     *  -> 리스트를 size 씩 나눈것들 중에 page 번째의 값을을 호출
     *  -> page=1, size=10 이라고 가정하면 10~19.
     */
    @GetMapping("/list")
    //    @Secured("ROLE_MANAGER")
    public ResponseEntity<?> getMemberList(Pageable pageable) {
        StatusNResultDTO result = memberService.getMemberList(pageable);
        if(result.isCheck()) {
            return ResponseEntity.ok(result.getList());
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("Content-Type", "text/plain")
                .body(result.getMessage());
        }
    }


}
