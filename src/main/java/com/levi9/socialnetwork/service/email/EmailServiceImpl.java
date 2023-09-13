package com.levi9.socialnetwork.service.email;

import com.levi9.socialnetwork.entity.EventInvitationEntity;
import com.levi9.socialnetwork.util.DateTimeUtil;
import com.levi9.socialnetwork.util.GenericMessages;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final ITemplateEngine thymeleafTemplateEngine;


    /**
     *
     * @throws MessagingException ako se desi greska prilikom slanja mejla.
     */
    @Override
    public void sendEventReminder(EventInvitationEntity eventInvitation) {
        Context context = new Context();
        context.setVariable("invitee", eventInvitation.getInvitee().getMember().getUsername());
        context.setVariable("location", eventInvitation.getEvent().getLocation());
        LocalDateTime eventTime = eventInvitation.getEvent().getDateTime();
        context.setVariable("time", DateTimeUtil.formatDateTime(eventTime));
        context.setVariable("startingInHours", 3);

        try {
            String htmlBody = thymeleafTemplateEngine.process("eventReminder.html", context);
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(eventInvitation.getInvitee().getMember().getEmail());
            helper.setSubject("[Social Network] Event Reminder");
            helper.setText(htmlBody, true);
            emailSender.send(message);
        } catch (MessagingException ex) {
            throw new com.levi9.socialnetwork.exception.customexception.MessagingException(GenericMessages.ERROR_SENDING_MAIL);
        }
    }

    /**
     *
     * @throws MessagingException ako se desi greska prilikom slanja mejla.
     */
    @Override
    public void sendEventNotification(EventInvitationEntity eventInvitation) {
        Context context = new Context();
        context.setVariable("group", eventInvitation.getEvent().getGroup().getName());
        context.setVariable("invitor", eventInvitation.getEvent().getCreator().getMember().getUsername());
        context.setVariable("invitee", eventInvitation.getInvitee().getMember().getUsername());
        context.setVariable("location", eventInvitation.getEvent().getLocation());
        LocalDateTime eventTime = eventInvitation.getEvent().getDateTime();
        context.setVariable("time", DateTimeUtil.formatDateTime(eventTime));
        context.setVariable("confirmUntil", DateTimeUtil.formatDateTime(eventTime.minus(24, ChronoUnit.HOURS)));

        try {
            String htmlBody = thymeleafTemplateEngine.process("eventInvitation.html", context);
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(eventInvitation.getInvitee().getMember().getEmail());
            helper.setSubject("[Social Network] Event Invitation");
            helper.setText(htmlBody, true);
            emailSender.send(message);
        } catch (MessagingException ex) {
            throw new com.levi9.socialnetwork.exception.customexception.MessagingException(GenericMessages.ERROR_SENDING_MAIL);
        }
    }
}