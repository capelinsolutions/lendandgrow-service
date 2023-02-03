package com.example.HardMoneyLending.query.property;

import com.example.HardMoneyLending.dto.property.PaginatedPropertyRequest;
import com.example.HardMoneyLending.dto.property.PaginatedPropertyResponse;
import com.example.HardMoneyLending.dto.property.PropertyFilterDTO;
import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.services.borrower.IBorrowerService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.property.Property;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.property.IPropertyService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Component
@Validated
@PreAuthorize("isAuthenticated()")
public class PropertyQueryResolver implements GraphQLQueryResolver {

    private final IPropertyService service;
    private final IUserService userService;

    private final IBorrowerService borrowerService;

    @Autowired
    public PropertyQueryResolver(final IPropertyService service, IUserService userService, IBorrowerService borrowerService) {
        this.service = service;
        this.userService = userService;
        this.borrowerService = borrowerService;
    }

    public ListMessage<Property> getAllProperty() throws EntityNotFoundException {
        return service.getAllProperty(true);
    }

    public Message<Property> getPropertyById(String id) throws EntityNotFoundException {
        return service.getPropertyById(id, true);
    }

    public ListMessage<Property> getAllPropertyByPropertyTypeId(String propertyTypeId) throws EntityNotFoundException {
        return service.getAllPropertyByPropertyTypeId(propertyTypeId, true);
    }

    public ListMessage<Property> getAllPropertyByBorrowerId(String borrowerId) throws EntityNotFoundException {
        return service.getAllPropertyByBorrowerId(borrowerId, true);
    }

    public Message<Property> getPropertyByIdForUpdate(String id) throws EntityNotFoundException {
        return service.getPropertyByIdForUpdate(id, true);
    }

    public ListMessage<Property> getAllPropertyForLoggedInUser() throws BadRequestException, EntityNotFoundException {
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        return service.getAllPropertyForLoggedInUser(user);
    }

    public Message<PaginatedPropertyResponse> getPropertyListByPagination(PaginatedPropertyRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNo(), request.getPageSize());
        User user = null;
        if(request.getBorrowerId()!=null){
            Borrower borrower = borrowerService.getBorrower(request.getBorrowerId());
            user = userService.getUserByUserId(borrower.getUserId());
        } else {
            user = userService.loadByUsername(getContext().getAuthentication().getName());
        }

        return service.getAllPropertyByPagination(request,user,pageable);
    }

    public Message<PaginatedPropertyResponse> getPropertyListByPaginationWithFilter(PropertyFilterDTO request) {
        Pageable pageable = PageRequest.of(request.getPageNo(), request.getPageSize());
        return service.getAllPropertyByPaginationWithFilter(request,pageable);
    }

    public Message<Integer> getTotalNoOfPropertiesOfBorrower() throws EntityNotFoundException {
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        Borrower borrower = borrowerService.getBorrowerByUserId(user.getUserId());
        return service.getTotalNoOfPropertiesOfBorrower(borrower.getBorrowerId());
    }
}
