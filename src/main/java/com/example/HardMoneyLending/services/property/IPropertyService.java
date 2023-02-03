package com.example.HardMoneyLending.services.property;

import com.example.HardMoneyLending.dto.property.*;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.models.property.Property;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

public interface IPropertyService {
    Message<Property> createProperty(CreatePropertyDTO dto) throws EntityNotFoundException, BadRequestException;

    boolean isPropertyExistsById(String propertyId) throws BadRequestException, EntityNotFoundException;

    Message<Property> getPropertyById(String id, boolean isActive) throws EntityNotFoundException;

    ListMessage<Property> getAllProperty(boolean isActive) throws EntityNotFoundException;

    ListMessage<Property> getAllPropertyByBorrowerId(String borrowerId, boolean isActive) throws EntityNotFoundException;

    ListMessage<Property> getAllPropertyByPropertyTypeId(String propertyTypeId, boolean isActive) throws EntityNotFoundException;

    Property save(Property property) throws BadRequestException;

    Message<Property> updateProperty(UpdatePropertyDTO dto) throws EntityNotFoundException, BadRequestException;

    Message<Property> getPropertyByIdForUpdate(String id, boolean b) throws EntityNotFoundException;

    List<String> storeImage(MultipartFile[] multipartFiles, String fileType, String propertyId) throws BadRequestException, com.example.HardMoneyLending.exception.general_exception.BadRequestException, com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException;

    ListMessage getAllPropertyForLoggedInUser(User user) throws EntityNotFoundException;

    Message<PaginatedPropertyResponse> getAllPropertyByPaginationWithFilter(PropertyFilterDTO request, Pageable pageable) throws EntityNotFoundException;

    Message<PaginatedPropertyResponse> getAllPropertyByPagination(PaginatedPropertyRequest request, User user, Pageable pageable) throws EntityNotFoundException;

    boolean isExistsInProperty(String name, String documentUrl) throws com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException, MalformedURLException;

    Message<Integer> getTotalNoOfPropertiesOfBorrower(String borrowerId) throws EntityNotFoundException;
}
