package com.example.HardMoneyLending.models.otp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "otp")
public class Otp {

    @Id
    private String id;

    private String userId;
    private String otp;
    private String otpType;
}
