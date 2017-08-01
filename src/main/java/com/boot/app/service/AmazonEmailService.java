package com.boot.app.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Vitor Oliveira on 2017
 */
@Service
public class AmazonEmailService {

    private static final Logger logger = LoggerFactory.getLogger(AmazonEmailService.class);

    private final String accessKey;
    private final String secretKey;

    @Autowired
    public AmazonEmailService(@Value("${amazon.ses.access.key}") String accessKey,
                              @Value("${amazon.ses.secret.key}") String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public void sendEmail(String textSubject, String textBody, String fromEmail, String... toEmail) {
        Destination destination = new Destination()
            .withToAddresses(toEmail);

        Content subject = new Content().withData(textSubject);
        Body body = new Body()
            .withText(new Content().withData(textBody));

        Message message = new Message()
            .withSubject(subject)
            .withBody(body);

        SendEmailRequest request = new SendEmailRequest()
            .withSource(fromEmail)
            .withDestination(destination)
            .withMessage(message);

        try {
            logger.info("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_WEST_1)
                .build();

            client.sendEmail(request);
            logger.info("Email sent!");

        } catch (Exception ex) {
            logger.error("The email was not sent.");
            logger.error("Error message: " + ex.getMessage());
        }
    }
}