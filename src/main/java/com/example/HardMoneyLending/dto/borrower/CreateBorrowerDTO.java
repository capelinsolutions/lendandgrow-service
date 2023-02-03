package com.example.HardMoneyLending.dto.borrower;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBorrowerDTO {
    private String userId;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String address;
    @NotBlank
    private String city;
    private String zip;
    private String state;
    @NotBlank
    private String country;
    @NotBlank
    private String contact;
    private String telephone;
    private LocalDate dob;
    private String gender;
    private String companyName;
    private String occupation;
}
