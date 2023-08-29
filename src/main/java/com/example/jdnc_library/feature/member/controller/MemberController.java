package com.example.jdnc_library.feature.member.controller;

import com.example.jdnc_library.domain.member.model.Role;
import com.example.jdnc_library.feature.member.DTO.MemberDTO;
import com.example.jdnc_library.feature.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Operation(summary = "유저 정보 요청", description = "현재 로그인 되어있는 유저의 정보를 리턴")
    public ResponseEntity<MemberDTO> getMember() {
        try {
            MemberDTO memberDTO = memberService.getMember();

            return ResponseEntity.ok(memberDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 로그인한 유저의 role을 리턴
     */
    @GetMapping("/role")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Role 요청", description = "현재 로그인 되어있는 유저의 Role 리턴")
    public String getRole() {
        try {
            Role role = memberService.getRole();

            return role.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 모든 유저 리스트 불러오기(페이징)
     * @param "page", "size"
     *  -> 리스트를 size 씩 나눈것들 중에 page 번째의 값을을 호출
     *  -> page=1, size=10 이라고 가정하면 10~19.
     */
    @GetMapping("/list")
    //    @Secured("ROLE_MANAGER")
    @Operation(summary = "유저 리스트 요청", description = "모든 유저 리스트를 리턴")
    public ResponseEntity<List<MemberDTO>> getMemberList(Pageable pageable) {
        try {
            List<MemberDTO> result = memberService.getMemberList(pageable);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/user")
//    @Secured("ROLE_MANAGER")
    @Operation(summary = "검색한 유저 정보 리턴", description = "검색한 이름과 일치하는 유저 정보를 리턴합니다")
    public List<MemberDTO> getSearchUserInfo(@RequestParam String name) {
        List<MemberDTO> searchUserInfo = memberService.getSearchUser(name);

        return searchUserInfo;
    }
}
