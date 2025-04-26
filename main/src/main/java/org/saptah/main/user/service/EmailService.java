package org.saptah.main.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender javamailsender;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${server.port}")
    private String serverPort;

    private String generateVerificationUrl(String token) {
        return new StringBuilder(serverUrl)
                .append(":")
                .append(serverPort)
                .append("/v1/activate/adminuser/")
                .append(token)
                .toString();
    }

    public boolean sendVerificationLinkEmailToAdminUser(String token, String to){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@saptah.org");
        message.setTo(to);
        message.setSubject("Verify your Saptah account");
        String verificationEndpointUrl = generateVerificationUrl(token);
        message.setText(
                "Click this link to confirm your email address.\n" +
                verificationEndpointUrl+"\n" +
                "The link will expire after 24 hours.");
        try{
            javamailsender.send(message);
            return true;
        } catch (MailException e) {
            System.out.println("Error while sending email: "+e.getMessage());
            return false;
        }
    }
}
