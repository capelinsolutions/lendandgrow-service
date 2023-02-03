package com.example.HardMoneyLending.repositories.user;

import com.example.HardMoneyLending.models.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    User findOneByEmailIgnoreCase(String email);
}
