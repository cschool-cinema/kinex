package pl.termosteam.kinex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {

    final private JavaMailSender mailSender;

    @Autowired
    public SendEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * This method will send compose and send the message
     */
    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
