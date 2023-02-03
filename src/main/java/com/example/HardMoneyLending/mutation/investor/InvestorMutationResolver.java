package com.example.HardMoneyLending.mutation.investor;

import com.example.HardMoneyLending.dto.investor.CreateInvestorDTO;
import com.example.HardMoneyLending.dto.investor.UpdateInvestorDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.exception.general_exception_graphql.UnprocessableException;
import com.example.HardMoneyLending.models.investor.Investor;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.investor.IInvestorService;
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
public class InvestorMutationResolver implements GraphQLMutationResolver {

    private final IInvestorService investorService;
    private final IUserService userService;

    @Autowired
    public InvestorMutationResolver(final IInvestorService investorService,
                                    final IUserService userService) {
        this.investorService = investorService;
        this.userService = userService;
    }

    public Message<Investor> createInvestor(CreateInvestorDTO createInvestorDTO) throws Exception {
        return investorService.createInvestor(createInvestorDTO);
    }

    public Message<Investor> deleteInvestor() throws Exception {
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        Investor investor = investorService.getInvestorByUserId(user.getUserId());
        try {
            Message<Investor> investorMessage = investorService.deleteInvestor(investor.getInvestorId());
            return investorMessage;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Message<Investor> updateInvestor(UpdateInvestorDTO updateInvestorDTO) throws UnprocessableException, BadRequestException, EntityNotFoundException {
        Message<Investor> investorMessage = investorService.updateInvestor(updateInvestorDTO);
        return investorMessage;
    }
}
