package com.example.HardMoneyLending.services.user;

import com.example.HardMoneyLending.dto.borrower.CreateBorrowerDTO;
import com.example.HardMoneyLending.dto.investor.CreateInvestorDTO;
import com.example.HardMoneyLending.dto.user.CreateUserDTO;
import com.example.HardMoneyLending.dto.user.user_type.UserTypeDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception.DateTimeException.DateNotValidException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.exception.general_exception_graphql.UnprocessableException;
import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.models.investor.Investor;
import com.example.HardMoneyLending.models.otp.Otp;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.models.user.UserType;
import com.example.HardMoneyLending.repositories.user.UserRepository;
import com.example.HardMoneyLending.repositories.user.user_type.UserTypeRepository;
import com.example.HardMoneyLending.services.borrower.IBorrowerService;
import com.example.HardMoneyLending.services.investor.IInvestorService;
import com.example.HardMoneyLending.services.mail.IEmailService;
import com.example.HardMoneyLending.services.otp.IOtpService;
import com.example.HardMoneyLending.utils.EmailTemplates;
import com.example.HardMoneyLending.utils.Enums;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.security.Principal;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserService implements IUserService, UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;
    private final IOtpService otpService;
    private final IBorrowerService borrowerService;
    private final IInvestorService investorService;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final UserTypeRepository userTypeRepository,
                       final PasswordEncoder passwordEncoder,
                       final IEmailService emailService,
                       final IOtpService otpService,
                       final IBorrowerService borrowerService,
                       final IInvestorService investorService) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.otpService = otpService;
        this.borrowerService = borrowerService;
        this.investorService = investorService;
    }

    //    Implementing Interface methods
    public Boolean isEmailUnique(String username) {
        return (!userRepository.findByEmail(username).isPresent());
    }

    public User getUser(Principal principal, String userId) {
        Optional<User> user = userRepository.findById(userId);
        return (user.isPresent() ? user.get() : null);
    }

    @Override
    public User getUserByUserId(String userId) throws EntityNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            log.debug("User not found with provided userId: " + userId );
            throw new EntityNotFoundException("User not found with provided user id.");
        }
        return user.get();
    }

    @Override
    public Message<String> getUserProfileImageByUSerId(String id) throws EntityNotFoundException{
        User user = getUserByUserId(id);
        if(user==null){
            log.error("User not found with "+id+" user id.");
            throw new EntityNotFoundException("User not found with "+id+" user id.");
        }
        UserType userType = getUserTypeById(user.getUserType());
        if (userType.getRole().equalsIgnoreCase(Enums.UserType.INVESTOR.toString())) {
            Investor investor = investorService.getInvestorByUserId(user.getUserId());
            return new Message<String>()
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString())
                    .setMessage("Profile Image Url fetched successfully.")
                    .setData(investor.getProfileImageURL());
        }else {
            Borrower borrower = borrowerService.getBorrowerByUserId(user.getUserId());
            return new Message<String>()
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString())
                    .setMessage("Profile Image Url fetched successfully.")
                    .setData(borrower.getProfileImageURL());
        }

    }

    public ListMessage<UserTypeDTO> getUserTypes() {
        List<UserType> userTypes = userTypeRepository.findAll();
        if (!userTypes.isEmpty()) {
            List<UserTypeDTO> userTypeDTOS = new ArrayList<UserTypeDTO>();
            for (UserType userType : userTypes) {
                UserTypeDTO userTypeDTO = new UserTypeDTO();
                userTypeDTO.setId(userType.getId());
                userTypeDTO.setRole(userType.getRole());
                userTypeDTOS.add(userTypeDTO);
            }
            return new ListMessage<UserTypeDTO>().setMessage("User Types Fetched").setStatus(200).setData(userTypeDTOS);
        }
        return null;

    }

    private boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    @Transactional
    @Override
    public Message<User> createUser(CreateUserDTO createUserDTO) throws BadRequestException {
        try {
            User user = new User();
            user.setName(createUserDTO.getName());

            String regexPattern = "^(.+)@(\\S+)$";
            boolean emailValid = patternMatches(createUserDTO.getEmail(), regexPattern);
            if(!emailValid){
                log.error("Email should be in standard format");
                throw new BadRequestException("Email should be in standard format");
            }

            user.setEmail(createUserDTO.getEmail());
            user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
            user.setContact(createUserDTO.getContact());
            user.setAddress(createUserDTO.getAddress());
            user.setCity(createUserDTO.getCity());
            user.setCountry(createUserDTO.getCountry());
            user.setUserType(createUserDTO.getUserType());

            // user.setIsActive(Boolean.TRUE);
            user = userRepository.save(user);

            log.info("User created with name: "+createUserDTO.getName());

            // If the registration is for borrower or for investor
            createBorrowerOrInvestor(user);
            // process OTP
            generateOTPByUserAndOTPFor(user, Enums.OTPFor.FOR_REGISTRATION);

            return new Message<User>()
                    .setMessage("User Created Successfully")
                    .setData(user)
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString());
        } catch (Exception e) {
            log.error("Error: "+e.getLocalizedMessage());
            throw new BadRequestException("User not created.");
        }

    }

    private void createBorrowerOrInvestor(User user) throws EntityNotFoundException, DateNotValidException, UnprocessableException, ParseException, BadRequestException {
        UserType userType = getUserTypeById(user.getUserType());
        if (userType.getRole().equalsIgnoreCase(Enums.UserType.BORROWER.toString())) {
            // Create borrower
            CreateBorrowerDTO dto = new CreateBorrowerDTO();
            dto.setEmail(user.getEmail());
            dto.setName(user.getName());
            dto.setUserId(user.getUserId());
            dto.setAddress(user.getAddress());
            dto.setCity(user.getCity());
            dto.setCountry(user.getCountry());
            dto.setContact(user.getContact());

            borrowerService.createBorrower(dto);
            log.info("Created borrower for the user.");
        }

        if (userType.getRole().equalsIgnoreCase(Enums.UserType.INVESTOR.toString())) {
            // Create Investor
            CreateInvestorDTO dto = new CreateInvestorDTO();
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setUserId(user.getUserId());
            dto.setCity(user.getCity());
            dto.setCountry(user.getCountry());
            dto.setContact(user.getContact());
            dto.setAddress(user.getAddress());

            investorService.createInvestor(dto);
            log.info("Created investor for the user.");
        }
    }

    @Override
    public void generateOTPByUserAndOTPFor(User user, Enums.OTPFor otpFor) throws BadRequestException, EntityNotFoundException {
        log.info("OTP sending process started...");

        // Create OTP for the user
        Otp otp = otpService.createOtp(user.getUserId(), otpFor);
        // Send otp on email
        try {
            emailService.sendMail(
                    user.getEmail(),
                    null,
                    Enums.OTPFor.FOR_REGISTRATION == otpFor ? "User registration OTP." : "User login OTP.",
                    EmailTemplates.OTP_TEMPLATE_L0 +
                            (Enums.OTPFor.FOR_REGISTRATION == otpFor ?
                                    EmailTemplates.OTP_REGISTRATION_TEMPLATE :
                                    EmailTemplates.OTP_LOGIN_TEMPLATE)
                            + otp.getOtp() + EmailTemplates.OTP_TEMPLATE_L1,
                    true
            );
            log.info("OTP sending process completed.");
        } catch (MessagingException e) {
            log.error("Error: "+e.getLocalizedMessage());
            log.info("OTP sending process completed.");
            throw new BadRequestException("Email sending process not worked.");
        }
    }

    @Override
    public Message<UserType> createUserType(String role) {
        Message<UserType> message = new Message<>();

        try {
            UserType userType = new UserType();
            userType.setRole(role);
            userType = userTypeRepository.save(userType);
            return new Message<UserType>().setMessage("User Type Created Successfully").setData(userType).setStatus(200);

        } catch (Exception e) {
            return new Message<UserType>().setMessage(e.getMessage()).setStatus(400);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkTransaction() throws Exception {
        try {
            User user = userRepository.findById("6212a4bdc3355a61eb83df33").get();
            user.setEmail("new_email2@mailinator.com");
            userRepository.save(user);
            throw new Exception();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserType getUserTypeById(String userTypeId) throws EntityNotFoundException {
        Optional<UserType> userType = userTypeRepository.findById(userTypeId);
        if (!userType.isPresent()) {
            log.debug("User type with userTypeId: "+ userTypeId+" not found.");
            throw new EntityNotFoundException("User type with provided user type id is not found");
        }
        log.debug("User type fetched successfully with userTypeId: "+userTypeId);
        return userType.get();
    }

    @Override
    public User loadByUsername(String email) throws BadRequestException {
        User user = userRepository.findOneByEmailIgnoreCase(email.trim());
        if(user == null)
            throw new BadRequestException("Provided email is not present.");
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("User logging in.");
        User user = userRepository.findOneByEmailIgnoreCase(username.trim());
        if (user == null) {
            log.error("Username: "+username+" is not found.");
            throw new UsernameNotFoundException("Authentication error username or password is incorrect.");
        }
        if (!user.getIsVerified()) {
            log.error("Username: "+username+" is not verified.");
            throw new UsernameNotFoundException("User is not verified. Please verify your account.");
        }

        if (!user.getIsActive()) {
            log.error("Username: "+username+" is not active.");
            throw new UsernameNotFoundException("User with username: "+username+" is not active");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getIsVerified(),
                true, true,
                true, getAuthorities()
        );
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        list.add(new SimpleGrantedAuthority(ROLE_PREFIX + "USER"));

        return list;
    }

    @Override
    public User save(User user) throws BadRequestException {
        try {
            user = userRepository.save(user);
            return user;
        } catch (Exception e) {
            log.error("Created user.");
            throw new BadRequestException("Server not responding.");
        }
    }
}