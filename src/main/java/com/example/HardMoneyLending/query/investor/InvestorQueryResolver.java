package com.example.HardMoneyLending.query.investor;

import graphql.kickstart.tools.GraphQLQueryResolver;
import com.example.HardMoneyLending.dto.investor.PaginatedInvestorRequest;
import com.example.HardMoneyLending.dto.investor.PaginatedInvestorResponse;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.investor.Investor;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.investor.IInvestorService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
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
public class InvestorQueryResolver implements GraphQLQueryResolver {

    private static final Integer PAGE_SIZE = 8;

    private final IInvestorService investorService;
    private final IUserService userService;

    @Autowired
    public InvestorQueryResolver(final IInvestorService investorService,
                                 final IUserService userService) {
        this.investorService = investorService;
        this.userService = userService;
    }

    public Message<PaginatedInvestorResponse> getInvestorListByPagination(PaginatedInvestorRequest dto) throws EntityNotFoundException {
        Pageable pageable = PageRequest.of(dto.getPageNo(), dto.getPageSize());
        return investorService.getAllInvestorByPagination(dto, pageable);
    }

    public Message<Investor> getInvestor() throws Exception{
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        Investor investor = investorService.getInvestorByUserId(user.getUserId());
        if (investor == null) {
            throw new Exception("Investor not found");
        }
        Message m = new Message();
        return m.setData(investor).setMessage("Successfully fetched.").setStatus(HttpStatus.OK.value()).setCode(HttpStatus.OK.toString());
    }

    public Message<Investor> getInvestorById(String id) throws Exception{
        Investor investor = investorService.getInvestor(id);
        Message m = new Message();
        return m.setData(investor).setMessage("Successfully fetched.").setStatus(HttpStatus.OK.value()).setCode(HttpStatus.OK.toString());
    }

    public ListMessage<Investor> getAllInvestor() throws EntityNotFoundException {
        List<Investor> data = investorService.getAllInvestor();
        ListMessage<Investor> response = new ListMessage<>();
        return response
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Investors list successfully fetched.")
                .setData(data);
    }
}
