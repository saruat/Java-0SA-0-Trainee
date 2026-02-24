package ru.saruat.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

        public void sendAccountCreatedEmail(String to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Добро пожаловать!");
            message.setText("Здравствуйте! Ваш аккаунт на сайте был успешно создан.");
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email to " + to, e);
        }
    }

    public void sendAccountDeletedEmail(String to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Аккаунт удален");
            message.setText("Здравствуйте! Ваш аккаунт удален.");
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send deletion email to " + to, e);
        }
    }

    /*private void sendEmail(String to, String subject, String text){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("no-reply@saruat.ru");
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email to " + to, e);
        }
    }*/

}
