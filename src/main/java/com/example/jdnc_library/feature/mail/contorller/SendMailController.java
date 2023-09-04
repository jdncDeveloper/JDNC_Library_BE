package com.example.jdnc_library.feature.mail.contorller;

import com.example.jdnc_library.feature.mail.DTO.MailServiceDTO;
import com.example.jdnc_library.feature.mail.service.EmailService;
import com.example.jdnc_library.security.model.PrincipalDetails;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
@EnableAsync
@Secured("ROLE_ADMIN")
@RequestMapping("/mail")
public class SendMailController {

    private final EmailService emailService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void sendMailTest(
        @RequestBody MailServiceDTO mailServiceDTO,
        @AuthenticationPrincipal PrincipalDetails principalDetails) throws MessagingException {
        emailService.sendMail(mailServiceDTO.getId(), principalDetails);
    }

}
