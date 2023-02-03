package com.example.HardMoneyLending.repositories.forget_password;

import com.example.HardMoneyLending.models.forget_password.ForgetPassword;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgetPasswordRepository extends MongoRepository<ForgetPassword, String> {

    ForgetPassword findByEmail(String trim);

    Boolean existsByEmailAndToken(String email, String token);
}
