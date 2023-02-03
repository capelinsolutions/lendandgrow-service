package com.example.HardMoneyLending.models.investor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "investor")
public class Investor {
    @Id
    private String investorId;
    private String userId;
    private Boolean isActive = false;
    private String name;
    private String email;
    private LocalDate dob;
    private String address;
    private String city;
    private String zip;
    private String state;
    private String country;
    private String contact;
    private String telephone;
    private String gender;
    private String occupation;
    private String serialNo;
    private String about;
    private String companyName;
    private String additionalLanguage;
    private String feeStructureId;
    private List<String> questions;
    private String profileImageURL;
    private Date createdAt;
    private Date updatedAt;
}
