package com.example.jdnc_library.feature.member.DTO;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagedMemberDTO {
    int totalPage;

    List<MemberDTO> MemberDTOList;
}
