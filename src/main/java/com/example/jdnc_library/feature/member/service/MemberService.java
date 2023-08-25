package com.example.jdnc_library.feature.member.service;

import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.domain.member.model.Role;
import com.example.jdnc_library.domain.member.repository.MemberRepository;
import com.example.jdnc_library.exception.clienterror._400.BadRequestException;
import com.example.jdnc_library.feature.member.DTO.MemberDTO;
import com.example.jdnc_library.security.model.PrincipalDetails;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberDTO> getMemberList(Pageable pageable) {
        Page<Member> memberList = memberRepository.findAll(pageable);

        List<MemberDTO> list = new ArrayList<>();
        if(!memberList.isEmpty()) {
            for(Member member : memberList) {
                MemberDTO nowMember = new MemberDTO();
                nowMember.setMbNumber(member.getUsername());
                nowMember.setName(member.getName());
                nowMember.setEmail(member.getEmail());
                nowMember.setRole(member.getRole());

                list.add(nowMember);
            }
        } else {
            throw new BadRequestException("유저가 존재하지 않습니다");
        }

        return list;
    }

    public MemberDTO getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        MemberDTO memberDTO = new MemberDTO();
        Member member = principalDetails.getMember();

        memberDTO.setName(member.getName());
        memberDTO.setMbNumber(member.getUsername());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setRole(member.getRole());

        return memberDTO;
    }

    public Role getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        Role role = principalDetails.getMember().getRole();
        return role;
    }
}
