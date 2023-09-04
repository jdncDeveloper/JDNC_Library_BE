package com.example.jdnc_library.feature.mail.service;

import com.example.jdnc_library.domain.book.model.BorrowInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class MakeMailTemplate {
    public String getMailTitle() {
        StringBuffer stringBuffer = new StringBuffer();
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();

        stringBuffer.append("더큰내일센터 ").append(currentMonth).append("월 도서 미반납자 대상 반납 협조 요청");
        return stringBuffer.toString();
    }

    public String getMailTemplate(String managerName, BorrowInfo borrowInfo) {
        StringBuffer stringBuffer = new StringBuffer();
        String userName = borrowInfo.getCreatedBy().getName();
        String bookTitle = borrowInfo.getCollectionInfo().getBookInfo().getTitle();
        Long bookNum = borrowInfo.getCollectionInfo().getBookNumber();
        LocalDateTime rentalDate = borrowInfo.getCreatedAt();

        stringBuffer.append("안녕하세요 ").append(userName).append("님").append("\n")
            .append("제주더큰내일센터 ").append(managerName).append(" 매니저 입니다.").append("\n")
            .append("도서 미반납이 확인되어 반납 요청 드립니다.\n\n")
            .append("도서명 : ").append(bookTitle).append("\n")
            .append("도서 코드번호 : ").append(bookNum).append("\n")
            .append("대여일자 : ").append(rentalDate).append("\n\n")
            .append("감사합니다.");

        return stringBuffer.toString();
    }

}
