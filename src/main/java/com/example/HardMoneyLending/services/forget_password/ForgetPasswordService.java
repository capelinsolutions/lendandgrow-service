package com.example.HardMoneyLending.services.forget_password;

import com.example.HardMoneyLending.dto.forget_password.ForgetPasswordRequestDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.models.forget_password.ForgetPassword;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.repositories.forget_password.ForgetPasswordRepository;
import com.example.HardMoneyLending.services.mail.IEmailService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.EmailTemplates;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class ForgetPasswordService implements IForgetPasswordService {

    @Value("${forgot.password.link}")
    private String FORGET_PASSWORD_LINK;

    @Value("${forgot.password.link.expiry.in.seconds}")
    private String FORGET_PASSWORD_LINK_EXPIRY;

    private final IUserService userService;
    private final ForgetPasswordRepository repo;
    private final TaskScheduler taskScheduler;
    private final IEmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ForgetPasswordService(IUserService userService, ForgetPasswordRepository repo, TaskScheduler taskScheduler, IEmailService emailService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.repo = repo;
        this.taskScheduler = taskScheduler;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Message generateForgetPasswordLink(String email) throws BadRequestException {
        User user = userService.loadByUsername(email.trim());

        // Check if already link is created for the user
        ForgetPassword savedForgetPassword = repo.findByEmail(email.trim());

        if (savedForgetPassword != null) {
            log.info("Forget password link for email: "+email+ " is already generated.");
            throw new BadRequestException("Forget password link for email: "+email+ " is already generated.");
        }

        String token = UUID.randomUUID().toString();

        String link = FORGET_PASSWORD_LINK + "?token="+token+"&email="+email;
        // Generate link and email
        ForgetPassword forgetPassword = ForgetPassword.builder()
                .link(link)
                .email(email.trim())
                .token(token)
                .build();

        try {
            final ForgetPassword savedData = repo.save(forgetPassword);
            emailService.sendMail(
                    email,
                    null,
                    "Forget password link",
                    EmailTemplates.FORGET_PASSWORD_TEMPLATE_L0 +
                            EmailTemplates.FORGET_PASSWORD_TEMPLATE +
                            link + EmailTemplates.FORGET_PASSWORD_TEMPLATE_L1 +
                            link + EmailTemplates.FORGET_PASSWORD_TEMPLATE_L2,
                    true);
            log.info("Forget password link will expired in 5 minute");
            taskScheduler.schedule(() -> {
                repo.deleteById(savedData.getId());
                log.info("Forget password link removed.");
                log.debug("Forget password link for user id: " + user.getUserId());
            },new Date(System.currentTimeMillis() +(1000 * Long.parseLong(FORGET_PASSWORD_LINK_EXPIRY))));
        } catch (Exception e) {
            log.error("ERROR: " + e.getLocalizedMessage());
            return new Message()
                    .setMessage("Forget password link can't be save some internal issue arises.")
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }

        return new Message()
                .setMessage("Forget password link generated successfully. Please check your email.")
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString());
    }

    @Override
    public Message saveForgetPassword(ForgetPasswordRequestDTO dto) throws BadRequestException{
        if(dto.getEmail()==null || dto.getEmail().isEmpty() ||
                dto.getToken()==null || dto.getToken().isEmpty() ||
                dto.getPassword()==null || dto.getPassword().isEmpty()){
            log.info("Email, password and token should not be null or empty..");
            throw new BadRequestException("Email, password and token should not be null or empty..");
        }
        Boolean isValid = repo.existsByEmailAndToken(dto.getEmail(),dto.getToken());
        if(isValid==true){
            try {
                User user = userService.loadByUsername(dto.getEmail());
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                userService.save(user);
                return new Message()
                        .setMessage("Forget password saved successfully.")
                        .setStatus(HttpStatus.OK.value())
                        .setCode(HttpStatus.OK.toString());
            }catch (Exception e) {
                log.error("ERROR: " + e.getLocalizedMessage());
                return new Message()
                        .setMessage("Forget password link can't be save some internal issue arises.")
                        .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            }

        }
        log.info("Forget password link for email: "+dto.getEmail()+ " is expired.");
        throw new BadRequestException("Forget password link for email: "+dto.getEmail()+ " is expired.");
    }

}
