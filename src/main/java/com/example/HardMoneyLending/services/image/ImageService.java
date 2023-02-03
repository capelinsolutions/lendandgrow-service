package com.example.HardMoneyLending.services.image;

import com.example.HardMoneyLending.constants.Constants;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.models.property.PropertyType;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.models.user.UserType;
import com.example.HardMoneyLending.models.investor.Investor;
import com.example.HardMoneyLending.services.borrower.IBorrowerService;
import com.example.HardMoneyLending.services.investor.IInvestorService;
import com.example.HardMoneyLending.services.property.property_type.IPropertyTypeService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.Enums;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;

@Slf4j
@Service
public class ImageService implements IImageService {

    @Value("${parent.resource.location}")
    private String PARENT_RESOURCES;

    @Value("${investor.resource.location}")
    private String INVESTOR_RESOURCES;

    @Value("${borrower.resource.location}")
    private String BORROWER_RESOURCES;

    @Value("${property.type.resource.location}")
    private String PROPERTY_TYPE_RESOURCES;

    @Value("${server.host.name}")
    private String SERVER_HOST_NAME;

    private final IInvestorService investorService;

    private final IBorrowerService borrowerService;

    private final IUserService userService;

    private final IPropertyTypeService propertyTypeService;

    public ImageService(final IInvestorService investorService,
                        final IUserService userService,
                        final IPropertyTypeService propertyTypeService,
                        final IBorrowerService borrowerService) {
        this.investorService = investorService;
        this.userService = userService;
        this.propertyTypeService = propertyTypeService;
        this.borrowerService = borrowerService;
    }

    /**
     * Taking multipart file for profile images of investor or borrower by getting userId and the multipart file
     * checking whether the user is a borrower type or investor type, so it will process as required. NOTE is the
     * user type not found or the user is not borrower or investor type then it will throw Entity not found and
     * Bad request exception.
     *
     * @param multipartFile
     * @param principal
     *
     * @exception EntityNotFoundException
     * @exception BadRequestException
     *
     * @return Message send with 200 status and message
     * */
    @Override
    public Message storeImage(MultipartFile multipartFile, String propertyTypeId, Principal principal) throws EntityNotFoundException, BadRequestException {
        User user = userService.loadByUsername(principal.getName());
        UserType userType = userService.getUserTypeById(user.getUserType());

        if (propertyTypeId != null && !propertyTypeId.isEmpty() && !propertyTypeId.equalsIgnoreCase("null")) {
            log.debug("Calling storePropertyTypeImage function.");
            return storePropertyTypeImage(multipartFile, propertyTypeId);
        } else if(userType.getRole().equalsIgnoreCase(Constants.BORROWER_TYPE)) {
            log.debug("Calling storeBorrowerProfileImage function.");
            return storeBorrowerProfileImage(multipartFile, user.getUserId());
        }else if(userType.getRole().equalsIgnoreCase(Constants.INVESTOR_TYPE)) {
            log.debug("Calling storeInvestorProfileImage function.");
            return storeInvestorProfileImage(multipartFile, user.getUserId());
        } else {
            log.debug("Image profile for provided user type can't be process.");
            Message m = new Message();
            return m.setStatus(HttpStatus.BAD_REQUEST.value())
                    .setCode(HttpStatus.BAD_REQUEST.toString())
                    .setMessage("Image profile for provided user type can't be process.");
        }
    }

    /**
     * Property type image storing operation
     * */
    private Message storePropertyTypeImage(MultipartFile multipartFile, String propertyTypeId) throws EntityNotFoundException, BadRequestException {
        PropertyType propertyType = propertyTypeService.findById(propertyTypeId);
        String iconUrl = processImage(multipartFile, Constants.PROPERTY_TYPE_TYPE);
        propertyType.setIconUrl(iconUrl);
        propertyTypeService.save(propertyType);
        return new Message().setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Icon is set for property type.")
                .setData(iconUrl);
    }

    /**
     * Investor profile image storing operation
     * */
    private Message storeInvestorProfileImage(MultipartFile multipartFile, String userId) throws EntityNotFoundException, BadRequestException {
        Investor investor = investorService.getInvestorByUserId(userId);
        // Get path of the image saved at local storage
        String profileImageUrl = processImage(multipartFile, Constants.INVESTOR_TYPE);

        investor.setProfileImageURL(profileImageUrl);

        investorService.save(investor);
        return new Message().setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Profile image set for investor.")
                .setData(profileImageUrl);

    }

    /**
     * Borrower profile image storing operation
     * */
    private Message storeBorrowerProfileImage(MultipartFile multipartFile, String userId)
            throws EntityNotFoundException, BadRequestException {
        // TODO borrower profile is not created yet so I'll implement this method after decided.
        Borrower borrower = borrowerService.getBorrowerByUserId(userId);
        // Get path of the image saved at local storage
        String profileImageUrl = processImage(multipartFile, Constants.BORROWER_TYPE);

        borrower.setProfileImageURL(profileImageUrl);
        borrowerService.save(borrower);

        return new Message().setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Profile image set for borrower.")
                .setData(profileImageUrl);
    }

    @Override
    public String processImage(MultipartFile multipartFile, String imageFor) throws BadRequestException {
        // Create directory for investor profile pics if not already exists.
        String directoryName = createDirectoryName(imageFor);

        File directory = new File(directoryName);

        // Check if the directories are already exists
        if (!directory.exists()) {
            directory.mkdirs();
        }
        long timestamp = System.currentTimeMillis();
        String absoluteFilePath = directoryName+"//"+timestamp+multipartFile.getOriginalFilename();

        File file = new File(absoluteFilePath);
        try {
            multipartFile.transferTo(file);
        } catch (Exception e) {
            log.error("File is not created successfully.");
            throw new BadRequestException("File can not be saved.");
        }
        String URL = null;
        if(imageFor.equalsIgnoreCase(Constants.PROPERTY_FILE_TYPE)) {
             URL = SERVER_HOST_NAME + "/" + Constants.DOWNLOAD_PROPERTY_FILE_URL + timestamp + multipartFile.getOriginalFilename() + "&type=";
        }else{
            URL = SERVER_HOST_NAME + "/" + Constants.DOWNLOAD_PROFILE_PIC_URL + timestamp + multipartFile.getOriginalFilename() + "&type=";
        }
        return createImageURL(imageFor, URL);
    }

    private String createDirectoryName(String imageFor) throws BadRequestException {
        if(imageFor.equalsIgnoreCase(Constants.INVESTOR_TYPE)) {
            log.debug("Directory name created: " + PARENT_RESOURCES + INVESTOR_RESOURCES + Constants.INVESTOR_PROFILE_PIC_DIRECTORY);
            return PARENT_RESOURCES + INVESTOR_RESOURCES + Constants.INVESTOR_PROFILE_PIC_DIRECTORY;
        }else if(imageFor.equalsIgnoreCase(Constants.BORROWER_TYPE)) {
            log.debug("Directory name created: " + PARENT_RESOURCES + BORROWER_RESOURCES + Constants.BORROWER_PROFILE_PIC_DIRECTORY);
            return PARENT_RESOURCES + BORROWER_RESOURCES + Constants.BORROWER_PROFILE_PIC_DIRECTORY;
        }else if(imageFor.equalsIgnoreCase(Constants.PROPERTY_TYPE_TYPE)) {
            log.debug("Directory name created: " + PARENT_RESOURCES + PROPERTY_TYPE_RESOURCES);
            return PARENT_RESOURCES + PROPERTY_TYPE_RESOURCES;
        }else if(imageFor.equalsIgnoreCase(Constants.PROPERTY_IMAGE_TYPE)) {
            log.debug("Directory name created: " + PARENT_RESOURCES + BORROWER_RESOURCES + Constants.BORROWER_PROPERTY_IMAGE_DIRECTORY);
            return PARENT_RESOURCES + BORROWER_RESOURCES + Constants.BORROWER_PROPERTY_IMAGE_DIRECTORY;
        }else if(imageFor.equalsIgnoreCase(Constants.PROPERTY_FILE_TYPE)) {
            log.debug("Directory name created: " + PARENT_RESOURCES + BORROWER_RESOURCES + Constants.BORROWER_PROPERTY_FILE_DIRECTORY);
            return PARENT_RESOURCES + BORROWER_RESOURCES + Constants.BORROWER_PROPERTY_FILE_DIRECTORY;
        }else {
            log.error("Image saving for type is not correct please provide valid one.");
            throw new BadRequestException("Image saving for type is not correct please provide valid one.");
        }
    }

    private String createImageURL(String imageFor, String URL) {
        if(imageFor.equalsIgnoreCase(Constants.BORROWER_TYPE)) {
            log.debug("Image url is created: " + URL + Constants.BORROWER_PROFILE_IMAGE);
            return URL + Constants.BORROWER_PROFILE_IMAGE;
        }

        if(imageFor.equalsIgnoreCase(Constants.PROPERTY_TYPE_TYPE)) {
            log.debug("Image url is created: " + URL + Constants.PROPERTY_TYPE_ICON_IMAGE);
            return URL + Constants.PROPERTY_TYPE_ICON_IMAGE;
        }

        if(imageFor.equalsIgnoreCase(Constants.PROPERTY_IMAGE_TYPE)){
            log.debug("Image url is created: " + URL + Constants.PROPERTY_IMAGE);
            return URL + Constants.PROPERTY_IMAGE;
        }

        if(imageFor.equalsIgnoreCase(Constants.PROPERTY_FILE_TYPE)) {
            log.debug("Image url is created: " + URL + Constants.PROPERTY_FILE);
            return URL + Constants.PROPERTY_FILE;
        }

        log.debug("Image url is created: " + URL + Constants.INVESTOR_PROFILE_IMAGE);
        return URL + Constants.INVESTOR_PROFILE_IMAGE;
    }

    @Override
    public byte[] getImage(String filename, String type) {
        String directoryPath = null;
        switch (type) {
            case Constants.INVESTOR_PROFILE_IMAGE:
                directoryPath = PARENT_RESOURCES + INVESTOR_RESOURCES + Constants.INVESTOR_PROFILE_PIC_DIRECTORY;
                break;
            case Constants.BORROWER_PROFILE_IMAGE:
                directoryPath = PARENT_RESOURCES + BORROWER_RESOURCES + Constants.BORROWER_PROFILE_PIC_DIRECTORY;
                break;
            case Constants.PROPERTY_TYPE_ICON_IMAGE:
                directoryPath = PARENT_RESOURCES + PROPERTY_TYPE_RESOURCES;
                break;
            case Constants.PROPERTY_IMAGE:
                directoryPath = PARENT_RESOURCES + BORROWER_RESOURCES + Constants.BORROWER_PROPERTY_IMAGE_DIRECTORY;
                break;
            case Constants.PROPERTY_FILE:
                directoryPath = PARENT_RESOURCES + BORROWER_RESOURCES + Constants.BORROWER_PROPERTY_FILE_DIRECTORY;
                break;
        }
        File file = new File(directoryPath+"//"+filename);
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error("Error: "+e.getLocalizedMessage());
            log.error("Error getting image file.");
            return null;
        }
    }

    @Override
    public Message deleteProfileImage(String userType, Principal principal) throws com.example.HardMoneyLending.exception.general_exception.BadRequestException {
        User user = userService.loadByUsername(principal.getName());
        Investor investor = null;
        Borrower borrower = null;
        if (userType.equalsIgnoreCase(Enums.UserType.INVESTOR.name())) {
            investor = investorService.getInvestorByUserId(user.getUserId());
            String imageUrl = investor.getProfileImageURL();
            if (imageUrl == null || imageUrl.isEmpty()) {
                log.info("No profile image found for user id: "+user.getUserId());
                throw new com.example.HardMoneyLending.exception.general_exception.BadRequestException("No profile image found for the current user.");
            }
            String filename = imageUrl.split("/?filename=")[1].split("&type=")[0];
            String directoryPath = PARENT_RESOURCES + INVESTOR_RESOURCES + Constants.INVESTOR_PROFILE_PIC_DIRECTORY + "//" + filename;
            File file = new File(directoryPath);
            file.delete();
            investor.setProfileImageURL(null);
            try {
                investorService.save(investor);
            } catch (BadRequestException e) {
                log.info("Investor profile image deleted from file system but not updated in our db.");
            }
        } else {
            borrower = borrowerService.getBorrowerByUserId(user.getUserId());
            String imageUrl = borrower.getProfileImageURL();
            if (imageUrl == null || imageUrl.isEmpty()) {
                log.info("No profile image found for user id: "+user.getUserId());
                throw new com.example.HardMoneyLending.exception.general_exception.BadRequestException("No profile image found for the current user.");
            }
            String filename = imageUrl.split("/?filename=")[1].split("&type=")[0];
            String directoryPath = PARENT_RESOURCES + BORROWER_RESOURCES + Constants.BORROWER_PROFILE_PIC_DIRECTORY +"//"+filename;
            File file = new File(directoryPath);
            file.delete();
            borrower.setProfileImageURL(null);
            try{
            borrowerService.save(borrower);
            } catch (BadRequestException e) {
                log.info("Borrower profile image deleted from file system but not updated in our db.");
            }
        }

        return new Message()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Image profile deleted successfully.");
    }
}
