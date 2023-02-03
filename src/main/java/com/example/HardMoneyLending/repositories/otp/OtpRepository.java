package com.example.HardMoneyLending.repositories.otp;

import com.example.HardMoneyLending.models.otp.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends MongoRepository<Otp, String> {

    Otp findByOtpAndUserIdAndOtpType(String otpValue, String userId, String otpType);

    Otp findByUserIdAndOtpType(String userId, String name);
}
