package com.example.HardMoneyLending.services.borrower;

import com.example.HardMoneyLending.dto.borrower.CreateBorrowerDTO;
import com.example.HardMoneyLending.dto.borrower.PaginatedBorrowerRequest;
import com.example.HardMoneyLending.dto.borrower.PaginatedBorrowerResponse;
import com.example.HardMoneyLending.dto.borrower.UpdateBorrowerDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotSavedException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotUpdateException;
import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBorrowerService {
    //    query
    Borrower getBorrower(String borrowerId) throws BadRequestException, EntityNotFoundException;

    //    mutation
    Message<Borrower> createBorrower(CreateBorrowerDTO createBorrowerDTO);

    Message<Borrower> deleteBorrower(String borrowerId);

    List<Borrower> getAllBorrower() throws EntityNotFoundException;

    Message<PaginatedBorrowerResponse> getAllBorrowerByPagination(PaginatedBorrowerRequest dto, Pageable pageable) throws EntityNotFoundException;

    boolean isBorrowerExists(String borrowerId) throws BadRequestException, EntityNotFoundException;

    Message<Borrower> updateBorrower(UpdateBorrowerDTO updateBorrowerDto) throws BadRequestException, EntityNotFoundException, EntityNotUpdateException;

    Borrower getBorrowerByUserId(String userId) throws EntityNotFoundException;

    Borrower save(Borrower borrower) throws BadRequestException;

    long countNoOfBorrowersByUserIdList(List<String> recipientIdList);
}
