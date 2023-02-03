package com.example.HardMoneyLending.services.forget_password;

import com.example.HardMoneyLending.dto.forget_password.ForgetPasswordRequestDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.utils.Message;

public interface IForgetPasswordService {

    Message generateForgetPasswordLink(String email) throws BadRequestException;

    Message saveForgetPassword(ForgetPasswordRequestDTO dto) throws BadRequestException;
}
