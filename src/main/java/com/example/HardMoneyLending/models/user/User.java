package com.example.HardMoneyLending.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document("user")
public class User {
    @Id
    private String userId;
    @Indexed(unique=true)
    private String name;
    private String email;
    private Boolean isVerified  = Boolean.FALSE;
    private Boolean isActive  = Boolean.TRUE;
    private String userType;
    @JsonIgnore
    private String password;
    private String contact;
    private String address;
    private String country;
    private String city;
}
