package com.example.jdnc_library.feature.mail.contorller;

import com.example.jdnc_library.feature.mail.DTO.MailTemplateDTO;
import com.example.jdnc_library.feature.mail.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
public class SendMailTestController {

    private final EmailService emailService;

    @GetMapping("/mail")
    @ResponseStatus(HttpStatus.OK)
    public MailTemplateDTO GetMailTemplate() {
        MailTemplateDTO mailTemplate = emailService.GetMailTemplate();
        return mailTemplate;
    }

    @PostMapping("/mail")
    @ResponseStatus(HttpStatus.OK)
    public void SendMailTest(@RequestBody MailTemplateDTO mailTemplateDTO) throws MessagingException {
        emailService.SendMail(mailTemplateDTO);
        emailService.SetMailTemplate(mailTemplateDTO);
    }

}
