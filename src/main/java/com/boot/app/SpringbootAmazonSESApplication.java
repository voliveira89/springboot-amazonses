package com.boot.app;

import com.boot.app.service.AmazonEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootAmazonSESApplication {

    private String from;
    private String to;
    private String subject;
    private String body;
    private String filePath;

    private final AmazonEmailService amazonEmailService;

    @Autowired
    public SpringbootAmazonSESApplication(AmazonEmailService amazonEmailService,
                                          @Value("${email.from}") String from,
                                          @Value("${email.to}") String to,
                                          @Value("${email.subject}") String subject,
                                          @Value("${email.body}") String body,
                                          @Value("${email.attachment.path}") String filePath) {
        this.amazonEmailService = amazonEmailService;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.filePath = filePath;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootAmazonSESApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> amazonEmailService.sendEmail(subject, body, from, to);
    }
}
