package com.example.HardMoneyLending.query.fee_structure;

import graphql.kickstart.tools.GraphQLQueryResolver;
import com.example.HardMoneyLending.models.fee_structure.FeeStructure;
import com.example.HardMoneyLending.services.fee_structure.IFeeStructureService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Component
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class FeeStructureQueryResolver implements GraphQLQueryResolver {

    private IFeeStructureService feeStructureService;

    @Autowired
    public FeeStructureQueryResolver(IFeeStructureService feeStructureService) {
        this.feeStructureService = feeStructureService;
    }

    public Message<FeeStructure> getFeeStructure(String id) {
        return feeStructureService.getFeeStructureById(id);
    }

    public ListMessage<FeeStructure> getAllFeeStructure() {
        return feeStructureService.getAllFeeStructure();
    }
}