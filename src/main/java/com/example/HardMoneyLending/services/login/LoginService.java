package com.example.HardMoneyLending.services.login;

import com.example.HardMoneyLending.exception.general_exception.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.otp.IOtpService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.Enums;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Service
public class LoginService implements ILoginService{

    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String BASIC_AUTH = "Basic Y2FsY2VydHM6dG9wc2VjcmV0";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${server.host.name}")
    private String DOMAIN_URL;

    private final IUserService userService;
    private final IOtpService otpService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(final IUserService userService,
                        IOtpService otpService, final PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.otpService = otpService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Verify the username and password and then generate the OTP
     * */
    @Override
    public void verifyCredentialsAndProcessOTP(String username, String password) throws BadRequestException {
        log.info("Verifying credentials and generating otp.");
        if((username == null ||username.isEmpty()) ||
                (password == null || password.isEmpty())
        ){
            log.error("Username, grantType or password are null or empty.");
            throw new BadRequestException("Username, grantType or password are null or empty.");
        }

        User user = userService.loadByUsername(username);
        if (user == null) {
            log.error("");
            throw new BadRequestException("User is not found with username: "+username);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.error("User credentials are not valid.");
            throw new BadRequestException("Provided user credentials are not valid.");
        }
        try {
            userService.generateOTPByUserAndOTPFor(user, Enums.OTPFor.FOR_LOGIN);
        } catch (com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException e) {
            throw new BadRequestException(e.getLocalizedMessage());
        } catch (com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public String login(String username, String password, String otp) throws BadRequestException, UnirestException {
        log.info("Logging in user via otp");
        if((username == null ||username.isEmpty()) ||
                (password == null || password.isEmpty())
        ){
            log.error("Username, grantType or password are null or empty.");
            throw new BadRequestException("Username, grantType or password are null or empty.");
        }
        User savedOTP = null;
        try {
            savedOTP = otpService.verifyOtp(username, otp, Enums.OTPFor.FOR_LOGIN);
        } catch (com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException e) {
            log.error("Error: "+e.getLocalizedMessage());
            throw new BadRequestException(e.getMessage());
        }

        // Hit the login api
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = null;
        try {
            response = Unirest
                    .post(DOMAIN_URL+"/oauth/token?grant_type="+GRANT_TYPE_PASSWORD+"&username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8"))
                    .header(AUTHORIZATION_HEADER, BASIC_AUTH)
                    .asString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (response == null || response.getStatus() != HttpStatus.OK.value()) {
            log.error("Login failed.");
            throw new BadRequestException("Login is faild because of some unintentional error.");
        }

        StringBuilder res = new StringBuilder();
        res.append(response.getBody(), 0, response.getBody().length());

        return res.toString();
    }
}
