package com.example.HardMoneyLending.mutation.borrower;

import com.example.HardMoneyLending.dto.borrower.UpdateBorrowerDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotUpdateException;
import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.borrower.IBorrowerService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.Message;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Component
@Validated
@PreAuthorize("isAuthenticated()")
public class BorrowerMutationResolver implements GraphQLMutationResolver {

    private final IBorrowerService borrowerService;
    private final IUserService userService;

    @Autowired
    public BorrowerMutationResolver(final IBorrowerService borrowerService,
                                    final IUserService userService) {
        this.borrowerService = borrowerService;
        this.userService = userService;
    }

    public Message<Borrower> updateBorrower(UpdateBorrowerDTO updateBorrowerDto) throws BadRequestException, EntityNotFoundException, EntityNotUpdateException {
        Message<Borrower> borrowerMessage = borrowerService.updateBorrower(updateBorrowerDto);
        return borrowerMessage;
    }

    public Message<Borrower> deleteBorrower() throws Exception {
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        Borrower borrower = borrowerService.getBorrowerByUserId(user.getUserId());
        try {
            Message<Borrower> borrowerMessage = borrowerService.deleteBorrower(borrower.getBorrowerId());
            return borrowerMessage;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
