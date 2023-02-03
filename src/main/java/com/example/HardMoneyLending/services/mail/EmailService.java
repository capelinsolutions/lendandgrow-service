package com.example.HardMoneyLending.services.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class EmailService implements IEmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(String email, String copyTo, String subject, String text, boolean isHtmlText) throws MessagingException {

        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg,true);

        helper.setTo(email);
        if(copyTo != null && !copyTo.isEmpty())
            helper.setCc(copyTo);
        helper.setSubject(subject);

        helper.setText(text,isHtmlText);

        try {
            javaMailSender.send(msg);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     *This method is created for emails with CC
     *
     * @param email
     * @param subject
     * @param copyTo
     * @param text
     * @param isHtmlText
     *
     * @author Qasim Ali
     * @throws MessagingException
     */
    @Override
    public void sendMailWithCC(String email, String subject, String copyTo, String text, boolean isHtmlText) throws MessagingException {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setCc(copyTo);
            helper.setText(text, isHtmlText);
            javaMailSender.send(msg);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
