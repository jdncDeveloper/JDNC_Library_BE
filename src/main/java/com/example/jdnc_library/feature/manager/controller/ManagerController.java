package com.example.jdnc_library.feature.manager.controller;

import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.feature.manager.model.MemberDTO;
import com.example.jdnc_library.feature.manager.model.StatusNResultDTO;
import com.example.jdnc_library.feature.manager.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    /**
     * 모든 유저 리스트 불러오기(페이징)
     * @param "page", "size"
     *  -> 리스트를 size 씩 나눈것들 중에 page 번째의 값을을 호출
     *  -> page=1, size=10 이라고 가정하면 10~19.
     */
    @GetMapping("/members")
    //    @Secured("ROLE_MANAGER")
    public ResponseEntity<?> getMemberList(Pageable pageable) {
        StatusNResultDTO result = managerService.getMemberList(pageable);
        if(result.isCheck()) {
            return ResponseEntity.ok(result.getList());
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("Content-Type", "text/plain")
                .body(result.getMessage());
        }
    }

    /**
     * 도서지기 목록 불러오기
     */
    @GetMapping("/book-keeper")
//    @Secured("ROLE_MANAGER")
    public ResponseEntity<?> getBookKeeper() {
        StatusNResultDTO result = managerService.getBookKeeper();
        if(result.isCheck()) {
            return ResponseEntity.ok(result.getList());
        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("Content-Type", "text/plain")
                .body(result.getMessage());
        }
    }

    /**
     * 도서지기 임명
     * @param "mbNumber"
     */
    @PostMapping("/book-keeper")
//    @Secured("ROLE_MANAGER")
    public ResponseEntity<String> setBookKeeper(@RequestBody MemberDTO bookKeeperDTO) {
        StatusNResultDTO result = managerService.setBookKeeper(bookKeeperDTO.getMbNumber());

        if (result.isCheck()) {
            return ResponseEntity.ok(result.getMessage());

        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("Content-Type", "text/plain")
                .body(result.getMessage());
        }
    }

    @DeleteMapping("/book-keeper")
//    @Secured("ROLE_MANAGER")
    public ResponseEntity<?> deleteBookKeeper(@RequestBody MemberDTO bookKeeperDTO) {
        StatusNResultDTO result = managerService.deleteBookKeeper(bookKeeperDTO.getMbNumber());

        if (result.isCheck()) {
            return ResponseEntity.ok(result.getMessage());

        } else {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("Content-Type", "text/plain")
                .body(result.getMessage());
        }
    }
}
