package com.example.jdnc_library.feature.mail.contorller;

import com.example.jdnc_library.feature.mail.DTO.MailServiceDTO;
import com.example.jdnc_library.feature.mail.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
@EnableAsync
public class SendMailController {

    private final EmailService emailService;

    @PostMapping("/mail")
    @ResponseStatus(HttpStatus.OK)
    public void sendMailTest(@RequestBody MailServiceDTO mailServiceDTO) throws MessagingException {
        emailService.sendMail(mailServiceDTO.getId());
    }

}
