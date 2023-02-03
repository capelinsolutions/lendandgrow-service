package com.example.HardMoneyLending.mutation.user;

import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.utils.Enums;
import graphql.kickstart.tools.GraphQLMutationResolver;
import com.example.HardMoneyLending.dto.user.CreateUserDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityAlreadyExistException;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.models.user.UserType;
import com.example.HardMoneyLending.services.otp.IOtpService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

@Slf4j
@Component
@Validated
public class UserMutationResolver implements GraphQLMutationResolver {

    private final IUserService userService;
    private final IOtpService otpService;

    @Autowired
    public UserMutationResolver(final IUserService userService,
                                final IOtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }


    /**
     * In this method we are creating User entity with the corresponding user type borrower/investor.
     * NOTE: We are not making isActive check of user to true it should be checked upon OTP.
     *
     * @param createUserDTO
     *
     * @return Message<User>
     * */
    public Message<User> createUser(@Valid CreateUserDTO createUserDTO) throws Exception {
        Boolean email = userService.isEmailUnique(createUserDTO.getEmail());
        if (!email) {
            throw new EntityAlreadyExistException("User already Exists");
        }

        return userService.createUser(createUserDTO);
    }

    public Message<String> resendOtp(String userId) throws EntityNotFoundException, BadRequestException {
        User user = userService.getUserByUserId(userId);
        userService.generateOTPByUserAndOTPFor(user, Enums.OTPFor.FOR_REGISTRATION);
        return new Message<String>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setData("OTP resented successfully.")
                .setMessage("OTP resented successfully.");
    }

    /**
     * Check whether the OTP is valid if valid then isActive or isVerified should be checked with corresponding
     * operations for registration or login.
     *
     * @param email
     * @param otpValue
     * */
    public Message<User> verifyOtp(@Valid String email, String otpValue) throws BadRequestException {
        return new Message<User>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("OTP is verified successfully.")
                .setData(otpService.verifyOtp(email, otpValue, Enums.OTPFor.FOR_REGISTRATION));
    }

    @PreAuthorize("isAuthenticated()")
    public Message<UserType> createUserType(String role) throws Exception {
        if (role.isEmpty()){
            throw new Exception("User Role required");
        }

        return userService.createUserType(role);
    }
}
