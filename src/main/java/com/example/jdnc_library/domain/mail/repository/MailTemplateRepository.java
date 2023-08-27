package com.example.jdnc_library.domain.mail.repository;

import com.example.jdnc_library.domain.mail.model.MailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailTemplateRepository extends JpaRepository<MailTemplate, Long> {

}
