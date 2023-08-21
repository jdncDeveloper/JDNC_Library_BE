package com.example.jdnc_library.feature.mail.contorller;

import com.example.jdnc_library.feature.mail.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
public class SendMailTestController {

    private final EmailService emailService;

    @GetMapping("/mail")
    @ResponseStatus(HttpStatus.OK)
    public void SendMailTest() throws MessagingException {
        emailService.sendMail();
    }

}
