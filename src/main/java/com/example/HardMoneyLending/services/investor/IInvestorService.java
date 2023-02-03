package com.example.HardMoneyLending.services.investor;

import com.example.HardMoneyLending.dto.investor.CreateInvestorDTO;
import com.example.HardMoneyLending.dto.investor.PaginatedInvestorRequest;
import com.example.HardMoneyLending.dto.investor.PaginatedInvestorResponse;
import com.example.HardMoneyLending.dto.investor.UpdateInvestorDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception.DateTimeException.DateNotValidException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.exception.general_exception_graphql.UnprocessableException;
import com.example.HardMoneyLending.models.investor.Investor;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.Part;
import java.text.ParseException;
import java.util.List;

public interface IInvestorService {
    //    Query
    Investor getInvestor(String investorId);

    Message<PaginatedInvestorResponse> getAllInvestorByPagination(PaginatedInvestorRequest dto, Pageable pageable) throws EntityNotFoundException;
    //    Mutation
    Message createInvestor(CreateInvestorDTO createInvestorDTO) throws DateNotValidException, ParseException, EntityNotFoundException, UnprocessableException;

    Message deleteInvestor(String investorId);

    Message<Investor> updateInvestor(UpdateInvestorDTO updateInvestorDTO) throws BadRequestException, EntityNotFoundException, UnprocessableException;

    List<Investor> getAllInvestor() throws EntityNotFoundException;

    Investor getInvestorByUserId(String userId) throws EntityNotFoundException, BadRequestException;

    Investor getInvestorById(String id) throws EntityNotFoundException, BadRequestException;

    Investor save(Investor investor) throws BadRequestException;

    boolean uploadProfile(List<Part> part, DataFetchingEnvironment environment);

    long countNoOfInvestorByUserIdList(List<String> recipientIdList);
}
