package com.example.HardMoneyLending.services.image;

import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.utils.Message;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface IImageService {
    Message storeImage(MultipartFile multipartFile, String propertyTypeId, Principal principal) throws EntityNotFoundException, BadRequestException;

    String processImage(MultipartFile multipartFile, String imageFor) throws BadRequestException;

    byte[] getImage(String filename, String type);

    Message deleteProfileImage(String userType, Principal principal) throws com.example.HardMoneyLending.exception.general_exception.BadRequestException;
}
