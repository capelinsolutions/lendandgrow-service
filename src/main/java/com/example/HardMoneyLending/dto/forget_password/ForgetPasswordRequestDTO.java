package com.example.HardMoneyLending.dto.forget_password;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgetPasswordRequestDTO {
    private String token;
    private String email;
    private String password;
}
