package com.example.jdnc_library.feature.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendMail() throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        //제목, 내용 설정
        helper.setSubject("smtp test");
        helper.setText("test", false);

        //수신자 설정(리스트 형태)
        //현재는 테스트용 코드
        List<String> toUserList = new ArrayList<>();
        toUserList.add("tlfqkfka12@naver.com");
        toUserList.add("tlfqkfka1357@gmail.com");

        //메일 전송(setTo 파라미터에 문자열 리스트를 넘기면 한번에 여러명에게 전송 가능)
        helper.setTo(toUserList.toArray(new String[toUserList.size()]));
        javaMailSender.send(message);
    }

}
