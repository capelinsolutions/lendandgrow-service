package com.example.HardMoneyLending.utils;

public class EmailTemplates {

    public static final String OTP_TEMPLATE_L0 = "<html>" +
            "<head>" +
            "</head>" +
            "<body>";
    public static final String OTP_LOGIN_TEMPLATE = "<h4>" +
            "    OTP for HML Login Process" +
            "</h4>" +
            "Your login otp is: ";
    public static final String OTP_REGISTRATION_TEMPLATE = "<h4>" +
            "    OTP for HML Registration Process" +
            "</h4>" +
            "Your registration otp is: ";
    public static final String OTP_TEMPLATE_L1 = "</body>" +
                                                "</html>";

    public static final String FORGET_PASSWORD_TEMPLATE_L0 = "<html>"+
            "<head>" +
            "</head>" +
            "<body>";

    public static final String FORGET_PASSWORD_TEMPLATE = "<h4>" +
            "     Below is the forget password link."+
            "</h4><br><a href=\"";

    public static final String FORGET_PASSWORD_TEMPLATE_L1 = "\">";

    public static final String FORGET_PASSWORD_TEMPLATE_L2 = "</a></body>" +
            "</html>";
}
