package com.example.HardMoneyLending.dto.investor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvestorDTO {
    @NotBlank
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
    @NotBlank
    private String state;
    @NotBlank
    private String country;
    @NotBlank
    private String contact;
    private String telephone;
    @NotBlank
    private LocalDate dob;
    @NotBlank
    private String gender;
    @NotBlank
    private String occupation;
    private String companyName;
    private String about;
    private String additionalLanguage;
    private String feeStructureId;
    private List<String> questions;
}
