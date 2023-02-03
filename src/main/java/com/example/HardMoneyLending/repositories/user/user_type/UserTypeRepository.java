package com.example.HardMoneyLending.repositories.user.user_type;
import com.example.HardMoneyLending.models.user.UserType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserTypeRepository extends MongoRepository<UserType, String> {
}
