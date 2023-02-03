package com.example.HardMoneyLending.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String userType;
    @NotBlank
    private String password;

    private String city;
    private String country;
    private String contact;
    private String address;
}
