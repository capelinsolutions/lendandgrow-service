package com.example.HardMoneyLending.services.otp;

import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.otp.Otp;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.utils.Enums;

public interface IOtpService {
    Otp createOtp(String userId, Enums.OTPFor otp) throws BadRequestException, EntityNotFoundException;

    User verifyOtp(String email, String otpValue, Enums.OTPFor otpFor) throws BadRequestException;
}
