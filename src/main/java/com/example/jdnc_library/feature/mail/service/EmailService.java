package com.example.jdnc_library.feature.mail.service;

import com.example.jdnc_library.domain.book.model.BorrowInfo;
import com.example.jdnc_library.domain.book.repository.BorrowRepository;
import com.example.jdnc_library.domain.mail.model.MailTemplate;
import com.example.jdnc_library.domain.mail.repository.MailTemplateRepository;
import com.example.jdnc_library.exception.clienterror._400.BadRequestException;
import com.example.jdnc_library.feature.mail.DTO.MailTemplateDTO;
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
    private final BorrowRepository borrowRepository;
    private final MailTemplateRepository mailTemplateRepository;

    public MailTemplateDTO GetMailTemplate() {
        List<MailTemplate> mailTemplate = mailTemplateRepository.findAll();
        if(mailTemplate.size() < 1) {
            throw new BadRequestException("현재 정해신 템플릿 없음");
        }
        MailTemplate template = mailTemplate.get(0);

        MailTemplateDTO mailTemplateDTO = new MailTemplateDTO();
        mailTemplateDTO.setTitle(template.getTitle());
        mailTemplateDTO.setContents(template.getContents());

        return mailTemplateDTO;
    }

    public void SendMail(MailTemplateDTO mailTemplateDTO) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        //제목, 내용 설정
        helper.setSubject(mailTemplateDTO.getTitle());
        helper.setText(mailTemplateDTO.getContents(), false);

        //수신자 설정(리스트 형태)
        List<BorrowInfo> currentlyBorrowedBooks = borrowRepository.findAllByAdminCheckIsFalse();
        List<String> toUserList = new ArrayList<>();
        for(BorrowInfo bi : currentlyBorrowedBooks) {
            toUserList.add(bi.getCreatedBy().getEmail());
        }

        //메일 전송(setTo 파라미터에 문자열 리스트를 넘기면 한번에 여러명에게 전송 가능)
        helper.setTo(toUserList.toArray(new String[toUserList.size()]));
        javaMailSender.send(message);
    }

    public void SetMailTemplate(MailTemplateDTO mailTemplateDTO) {
        List<MailTemplate> mailTemplate = mailTemplateRepository.findAll();

        MailTemplate template = mailTemplate.get(0);

        template.update(mailTemplateDTO.getTitle(), mailTemplateDTO.getContents());
        mailTemplateRepository.save(template);
    }
}
