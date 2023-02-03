package com.example.HardMoneyLending.services.user;

import com.example.HardMoneyLending.dto.user.CreateUserDTO;
import com.example.HardMoneyLending.dto.user.user_type.UserTypeDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.models.user.UserType;
import com.example.HardMoneyLending.utils.Enums;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;

import java.security.Principal;

public interface IUserService {

    //query
    User getUser(Principal principal, String userId);
    User getUserByUserId(String userId) throws EntityNotFoundException;

    Message<String> getUserProfileImageByUSerId(String id) throws EntityNotFoundException;

    ListMessage<UserTypeDTO> getUserTypes();
    Boolean isEmailUnique(String email);

    //mutation
    Message createUser(CreateUserDTO createUserDTO) throws BadRequestException;

    void generateOTPByUserAndOTPFor(User user, Enums.OTPFor otpFor) throws BadRequestException, EntityNotFoundException;

    Message createUserType(String role);

    void checkTransaction() throws Exception;

    UserType getUserTypeById(String userTypeId) throws EntityNotFoundException;

    User loadByUsername(String email) throws BadRequestException;

    User save(User user) throws BadRequestException;
}
