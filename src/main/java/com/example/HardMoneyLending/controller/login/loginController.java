package com.example.HardMoneyLending.controller.login;

import com.example.HardMoneyLending.exception.general_exception.BadRequestException;
import com.example.HardMoneyLending.services.login.ILoginService;
import com.example.HardMoneyLending.utils.Message;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.entity.ContentProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/login")
public class loginController {

    private final ILoginService loginService;

    public loginController(ILoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/")
    public ResponseEntity loginForOTP(@RequestParam("username") String username,
                                      @RequestParam("password") String password) throws BadRequestException {
        loginService.verifyCredentialsAndProcessOTP(username,  password);
        return ResponseEntity.ok(new Message<String>()
                .setMessage("OTP successfully created for login.")
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setData("OTP CREATED.")
        );
    }

    @PostMapping(value = "/login-with-otp", produces = "application/json")
    public ResponseEntity login(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                @RequestParam("otp") String otp) throws BadRequestException, UnirestException {
        return ResponseEntity.ok(loginService.login(username, password, otp));
    }
}
