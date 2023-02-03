package com.example.HardMoneyLending.models.forget_password;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "forgot_password")
public class ForgetPassword {

    @Id
    private String id;
    private String token;
    private String link;
    private String email;
}
