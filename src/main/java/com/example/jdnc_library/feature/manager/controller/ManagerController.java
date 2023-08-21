package com.example.jdnc_library.feature.manager.controller;

import com.example.jdnc_library.feature.member.model.MemberDTO;
import com.example.jdnc_library.feature.manager.model.StatusNResultDTO;
import com.example.jdnc_library.feature.manager.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
     * 도서지기 목록 불러오기
     */
    @GetMapping("/book-keeper")
//    @Secured("ROLE_MANAGER")
    @Operation(summary = "도서지기 정보 요청", description = "현재 임명되어 있는 도서지기들의 정보를 리턴")
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
    @Operation(summary = "도서지기 임명", description = "해당 유저를 도서지기로 임명(Role 변경)")
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
    @Operation(summary = "도서지기 해임", description = "해당 유저를 도서지기에서 해임(Role 변경)")
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
