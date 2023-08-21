package com.example.jdnc_library.feature.manager.service;

import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.domain.member.model.Role;
import com.example.jdnc_library.domain.member.repository.MemberRepository;
import com.example.jdnc_library.feature.member.model.MemberDTO;
import com.example.jdnc_library.feature.manager.model.StatusNResultDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final MemberRepository memberRepository;

    public StatusNResultDTO getBookKeeper() {
        StatusNResultDTO newReturn = new StatusNResultDTO();
        List<Member> bookKeepers = memberRepository.findAllByRole(Role.valueOf("ROLE_BOOKKEEPER"));

        if(!bookKeepers.isEmpty()) {
            List<MemberDTO> list = new ArrayList<>();
            for(Member bookKeeper : bookKeepers) {
                MemberDTO bookKeeperDTO = new MemberDTO();
                bookKeeperDTO.setMbNumber(bookKeeper.getMbNumber());
                bookKeeperDTO.setName(bookKeeper.getName());
                bookKeeperDTO.setEmail(bookKeeper.getEmail());
                bookKeeperDTO.setRole(bookKeeper.getRole());

                list.add(bookKeeperDTO);
            }

            newReturn.setCheck(true);
            newReturn.setList(list);
        } else {
            newReturn.setCheck(false);
            newReturn.setMessage("현재 임명된 도서지기가 없습니다");
        }

        return newReturn;
    }

    public StatusNResultDTO setBookKeeper(String mbNumber) {
        StatusNResultDTO newReturn = new StatusNResultDTO();

        Member newBookKeeper = memberRepository.findByMbNumber(mbNumber);
        if(newBookKeeper == null) {
            newReturn.setCheck(false);
            newReturn.setMessage("해당 유저가 없습니다");
            return newReturn;
        }
        if(!newBookKeeper.getRole().equals("ROLE_BOOKKEEPER")) {
            newBookKeeper.updateROLE(Role.valueOf("ROLE_BOOKKEEPER"));
            memberRepository.save(newBookKeeper);
            newReturn.setCheck(true);
            newReturn.setMessage("해당 유저를 도서지기로 임명하였습니다");
        } else {
            newReturn.setCheck(false);
            newReturn.setMessage("해당 유저는 이미 도서지기이거나 매니저입니다");
        }
        return newReturn;
    }

    public StatusNResultDTO deleteBookKeeper(String mbNumber) {
        StatusNResultDTO newReturn = new StatusNResultDTO();

        Member newBookKeeper = memberRepository.findByMbNumber(mbNumber);
        if(newBookKeeper == null) {
            newReturn.setCheck(false);
            newReturn.setMessage("해당 유저가 없습니다");
            return newReturn;
        }
        if(newBookKeeper.getRole().equals("ROLE_BOOKKEEPER")) {
            newBookKeeper.updateROLE(Role.valueOf("ROLE_USER"));
            memberRepository.save(newBookKeeper);
            newReturn.setCheck(true);
            newReturn.setMessage("해당 유저를 일반 유저로 되돌렸습니다");
        } else {
            newReturn.setCheck(false);
            newReturn.setMessage("해당 유저는 도서지기가 아닙니다");
        }
        return newReturn;
    }
}
