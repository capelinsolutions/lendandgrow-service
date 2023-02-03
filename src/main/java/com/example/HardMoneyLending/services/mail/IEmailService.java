package com.example.HardMoneyLending.services.mail;

import javax.mail.MessagingException;

public interface IEmailService {
    void sendMail(String email, String copyTo, String subject, String text, boolean isHtmlText) throws MessagingException;

    void sendMailWithCC(String email, String subject, String copyTo, String text, boolean isHtmlText) throws MessagingException;
}
