package com.levi9.socialnetwork.util.notifications;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService{
    private final String USERNAME = "Levi9 Social Network <levi9praksamail@gmail.com>";

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(Notification notification) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setFrom(USERNAME);
            helper.setTo(notification.getEmailTo());
            helper.setSubject(notification.getSubject());
            helper.setText(notification.getText());

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}