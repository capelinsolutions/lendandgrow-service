package com.example.HardMoneyLending.services.login;

import com.example.HardMoneyLending.exception.general_exception.BadRequestException;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface ILoginService {
    void verifyCredentialsAndProcessOTP(String username, String password) throws BadRequestException;

    String login(String username, String password, String otp) throws BadRequestException, UnirestException;
}
