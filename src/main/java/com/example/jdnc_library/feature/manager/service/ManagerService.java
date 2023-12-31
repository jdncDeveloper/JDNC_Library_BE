package com.example.jdnc_library.feature.manager.service;

import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.domain.member.model.Role;
import com.example.jdnc_library.domain.member.repository.MemberRepository;
import com.example.jdnc_library.exception.clienterror._400.BadRequestException;
import com.example.jdnc_library.feature.member.DTO.MemberDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final MemberRepository memberRepository;

    public List<MemberDTO> getBookKeeper() {
        List<Member> bookKeepers = memberRepository.findAllByRole(Role.valueOf("ROLE_BOOKKEEPER"));

        List<MemberDTO> list = new ArrayList<>();
        if(!bookKeepers.isEmpty()) {
            for(Member bookKeeper : bookKeepers) {
                MemberDTO bookKeeperDTO = new MemberDTO();
                bookKeeperDTO.setMbNumber(bookKeeper.getUsername());
                bookKeeperDTO.setName(bookKeeper.getName());
                bookKeeperDTO.setEmail(bookKeeper.getEmail());
                bookKeeperDTO.setRole(bookKeeper.getRole());

                list.add(bookKeeperDTO);
            }
        } else {
            throw new BadRequestException("현재 임명된 도서지기가 없습니다");
        }

        return list;
    }

    public String setBookKeeper(String mbNumber) {
        Member newBookKeeper = memberRepository.findByUsername(mbNumber);
        if(newBookKeeper == null) {
            throw new BadRequestException("해당 유저가 없습니다");
        }
        if(!newBookKeeper.getRole().toString().equals("ROLE_BOOKKEEPER")) {
            newBookKeeper.updateROLE(Role.valueOf("ROLE_BOOKKEEPER"));
            memberRepository.save(newBookKeeper);
        } else {
            throw new BadRequestException("해당 유저는 이미 도서지기이거나 매니저입니다");
        }

        return "해당 유저를 도서지기로 임명하였습니다";
    }

    public String deleteBookKeeper(String mbNumber) {
        Member newBookKeeper = memberRepository.findByUsername(mbNumber);
        if(newBookKeeper == null) {
            throw new BadRequestException("해당 유저가 없습니다");
        }
        System.out.println(newBookKeeper.getRole());
        if(newBookKeeper.getRole().toString().equals("ROLE_BOOKKEEPER")) {
            newBookKeeper.updateROLE(Role.valueOf("ROLE_USER"));
            memberRepository.save(newBookKeeper);
        } else {
            throw new BadRequestException("해당 유저는 도서지기가 아닙니다");
        }
        return "해당 유저를 일반 유저로 되돌렸습니다";
    }
}
