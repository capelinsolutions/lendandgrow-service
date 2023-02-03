package com.example.HardMoneyLending.services.otp;

import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.otp.Otp;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.repositories.otp.OtpRepository;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.Enums;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Slf4j
@Service
public class OtpService implements IOtpService{

    @Value("${reg.otp.expiry-time.in.seconds}")
    private String EXPIRY_TIME_FOR_REG_OTP;

    private final OtpRepository repo;
    private final IUserService userService;
    private final TaskScheduler taskScheduler;

    public OtpService(final OtpRepository repo,
                      @Lazy final IUserService userService,
                      final TaskScheduler taskScheduler) {
        this.repo = repo;
        this.userService = userService;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public Otp createOtp(String userId, Enums.OTPFor otpFor) throws BadRequestException, EntityNotFoundException {
        // Check whether the user is already verified
        User user = userService.getUserByUserId(userId);

        if(user.getIsActive() && user.getIsVerified() && otpFor != Enums.OTPFor.FOR_LOGIN)
            throw new BadRequestException("User is already active and verified.");

        // Check if the otp already exists
        Otp savedOtp1 = repo.findByUserIdAndOtpType(userId, otpFor.name());
        Otp otp = new Otp();
        if (savedOtp1 != null &&
                (savedOtp1.getOtpType() != null ?
                        savedOtp1.getOtpType().equalsIgnoreCase(otpFor.name())
                        : false)
        ) {
            log.info("Resending otp.");
            otp = savedOtp1;
            // Check if it is for login
            /*if (Enums.OTPFor.FOR_LOGIN.name().equalsIgnoreCase(savedOtp1.getOtpType())) {
                log.info("Updating otp for login user");
                otp = savedOtp1;
            }else {
                log.error("Already exists the otp for user id: " + userId);
                throw new BadRequestException("OTP created already for user: " + user.getName());
            }*/
        }
        otp.setUserId(userId);
        otp.setOtp(String.format("%06d", new Random().nextInt(999999)));
        otp.setOtpType(otpFor.name());
        return save(userId, otp);
    }

    @NotNull
    private Otp save(String userId, Otp otp) throws BadRequestException{
        try {
            if(otp.getId() != null && !otp.getOtp().isEmpty())
                repo.deleteById(otp.getId());
            final Otp savedOtp = repo.save(otp);
            // Remove the otp after creation
            log.info("Otp removed in a minute");
            taskScheduler.schedule(() -> {
                repo.deleteById(savedOtp.getId());
                log.info("Otp removed.");
                log.debug("Otp removed for user id: " + userId);
            },new Date(System.currentTimeMillis() +(1000 * Long.parseLong(EXPIRY_TIME_FOR_REG_OTP))));
            return savedOtp;
        } catch (Exception e) {
            log.error("Error: "+e.getLocalizedMessage());
            throw new BadRequestException("Otp creation operation failed.");
        }
    }

    @Override
    public User verifyOtp(String email, String otpValue, Enums.OTPFor otpFor) throws BadRequestException {
        User user = userService.loadByUsername(email);
        Otp otp = null;

        if (otpFor == Enums.OTPFor.FOR_LOGIN && user.getIsActive()) {
            log.info("Fetching otp for login process.");
            otp = repo.findByOtpAndUserIdAndOtpType(otpValue, user.getUserId(), Enums.OTPFor.FOR_LOGIN.name());
        }else{
            log.info("Fetching otp for registration process.");
            otp = repo.findByOtpAndUserIdAndOtpType(otpValue, user.getUserId(), Enums.OTPFor.FOR_REGISTRATION.name());
        }

        if(otp == null) {
            log.error("Otp is expired or not valid.");
            throw new BadRequestException("Otp is expired or not valid.");
        }

        log.info("Otp is verified.");
        repo.delete(otp);

        if (otpFor == Enums.OTPFor.FOR_REGISTRATION){
            // Update user isVerified to true
            user.setIsVerified(Boolean.TRUE);
        }
        user = userService.save(user);
        log.info("Deleted the otp after verification.");
        return user;
    }
}
