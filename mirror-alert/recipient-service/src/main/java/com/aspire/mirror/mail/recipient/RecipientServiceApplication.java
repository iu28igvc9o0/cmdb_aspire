package com.aspire.mirror.mail.recipient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@EnableScheduling
public class RecipientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecipientServiceApplication.class, args);
    }
}
