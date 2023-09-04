package com.example.jdnc_library.feature.mail.service;

import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.domain.book.repository.BorrowRepository;
import com.example.jdnc_library.domain.member.repository.MemberRepository;
import com.example.jdnc_library.exception.clienterror._400.EntityNotFoundException;
import com.example.jdnc_library.security.model.PrincipalDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    private final BorrowRepository borrowRepository;

    private final MemberRepository memberRepository;

    private final MakeMailTemplate makeMailTemplate;

    @Async
    public void sendMail(long id, PrincipalDetails principalDetails) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        BorrowInfo borrowInfo = borrowRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, BorrowInfo.class));

        String adminName = principalDetails.getMember().getName();

        //제목, 내용 설정
        String title = makeMailTemplate.getMailTitle();
        String contents = makeMailTemplate.getMailTemplate(adminName, borrowInfo);
        helper.setSubject(title);
        helper.setText(contents, false);

        //수신자 설정(리스트 형태)
        String userEmail = borrowInfo.getCreatedBy().getEmail();

        //메일 전송(setTo 파라미터에 문자열 리스트를 넘기면 한번에 여러명에게 전송 가능)
        helper.setTo(userEmail);
        javaMailSender.send(message);
    }
}
