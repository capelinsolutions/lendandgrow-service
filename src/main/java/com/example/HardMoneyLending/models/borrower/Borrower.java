package com.example.HardMoneyLending.models.borrower;

import com.example.HardMoneyLending.models.property.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "borrower")
public class Borrower {
    @Id
    private String borrowerId;
    private String userId;
    private Boolean isActive = false;
    private String email;
    private String name;
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
    private String companyName;
    private String profileImageURL;

    // This field is only used when a new property type is created if it is not in the list
    private Set<PropertyType> propertyType;
    private Date createdAt;
    private Date updatedAt;
}
