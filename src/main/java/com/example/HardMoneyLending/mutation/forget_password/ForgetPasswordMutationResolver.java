package com.example.HardMoneyLending.mutation.forget_password;

import com.example.HardMoneyLending.dto.forget_password.ForgetPasswordRequestDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.services.forget_password.IForgetPasswordService;
import com.example.HardMoneyLending.utils.Message;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ForgetPasswordMutationResolver implements GraphQLMutationResolver {

    private final IForgetPasswordService service;


    public ForgetPasswordMutationResolver(IForgetPasswordService service) {
        this.service = service;
    }

    public Message createForgetPasswordLink(String email) throws BadRequestException {
        return service.generateForgetPasswordLink(email);
    }

    public Message updateForgetPassword(ForgetPasswordRequestDTO forgetPasswordRequestDTO) throws BadRequestException{
        return service.saveForgetPassword(forgetPasswordRequestDTO);
    }
}
