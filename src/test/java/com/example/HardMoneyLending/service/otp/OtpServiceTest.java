package com.example.HardMoneyLending.service.otp;

import com.example.HardMoneyLending.HardMoneyLendingApplication;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.otp.IOtpService;
import com.example.HardMoneyLending.utils.Enums;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HardMoneyLendingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OtpServiceTest {

    private String username;
    private String otp;
    private Enums.OTPFor otpFor;

    @Autowired
    private IOtpService otpService;

    @Before
    public void init() {
        username = "junaid11@mailinator.com";
        otp = "082666";
        otpFor = Enums.OTPFor.FOR_LOGIN;
    }

    @Test
    public void verifyOtp() {
        User user = otpService.verifyOtp(username, otp, otpFor);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(username);
    }
}
