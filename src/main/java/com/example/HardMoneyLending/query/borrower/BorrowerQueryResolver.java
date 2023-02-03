package com.example.HardMoneyLending.query.borrower;

import com.example.HardMoneyLending.dto.borrower.PaginatedBorrowerRequest;
import com.example.HardMoneyLending.dto.borrower.PaginatedBorrowerResponse;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.models.shared_file.SharedFile;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.borrower.IBorrowerService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import java.util.List;

@Slf4j
@Component
@Validated
@PreAuthorize("isAuthenticated()")
    public class BorrowerQueryResolver implements GraphQLQueryResolver {

    private static final Integer PAGE_SIZE = 8;

    private final IBorrowerService borrowerService;
    private final IUserService userService;

    @Autowired
    public BorrowerQueryResolver(final IBorrowerService borrowerService,
                                 final IUserService userService) {
        this.borrowerService = borrowerService;
        this.userService = userService;
    }

    public Message<PaginatedBorrowerResponse> getBorrowerListByPagination(PaginatedBorrowerRequest dto) throws EntityNotFoundException {
        Pageable pageable = PageRequest.of(dto.getPageNo(), dto.getPageSize());
        return borrowerService.getAllBorrowerByPagination(dto, pageable);
    }

    public Message<Borrower> getBorrower() throws EntityNotFoundException, BadRequestException {
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        Borrower borrower = borrowerService.getBorrowerByUserId(user.getUserId());
        return new Message<Borrower>()
                .setData(borrower)
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Successfully Fetched.");
    }

    public Message<Borrower> getBorrowerById(String id) throws EntityNotFoundException, BadRequestException {
        Borrower borrower = borrowerService.getBorrower(id);
        return new Message<Borrower>()
                .setData(borrower)
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Successfully Fetched.");
    }

    public ListMessage<Borrower> getAllBorrower() throws EntityNotFoundException {
        List<Borrower> data = borrowerService.getAllBorrower();
        ListMessage<Borrower> response = new ListMessage<>();
        return response
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Borrower list successfully fetched.")
                .setData(data);
    }

}
