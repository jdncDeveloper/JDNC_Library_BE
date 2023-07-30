package com.example.jdnc_library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class JdncLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdncLibraryApplication.class, args);
    }

}
