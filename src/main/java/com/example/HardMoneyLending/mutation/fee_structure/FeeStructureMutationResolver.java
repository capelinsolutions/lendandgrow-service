package com.example.HardMoneyLending.mutation.fee_structure;

import graphql.kickstart.tools.GraphQLMutationResolver;
import com.example.HardMoneyLending.dto.fee_structure.CreateFeeStructureDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotSavedException;
import com.example.HardMoneyLending.exception.general_exception_graphql.UnprocessableException;
import com.example.HardMoneyLending.models.fee_structure.FeeStructure;
import com.example.HardMoneyLending.services.fee_structure.IFeeStructureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Slf4j
@Component
@Validated
@PreAuthorize("isAuthenticated()")
public class FeeStructureMutationResolver implements GraphQLMutationResolver {

    private final IFeeStructureService feeStructureService;

    @Autowired
    public FeeStructureMutationResolver(final IFeeStructureService feeStructureService) {
        this.feeStructureService = feeStructureService;
    }

    public com.example.HardMoneyLending.utils.Message<FeeStructure> createFeeStructure(CreateFeeStructureDTO createFeeStructureDTO) throws EntityNotSavedException {
        com.example.HardMoneyLending.utils.Message<FeeStructure> feeStructureMessage = feeStructureService.createFeeStructure(createFeeStructureDTO);
        return feeStructureMessage;

    }

    public com.example.HardMoneyLending.utils.Message<FeeStructure> deleteFeeStructure(@NotBlank String feeStructureId) throws EntityNotFoundException, UnprocessableException {
        com.example.HardMoneyLending.utils.Message<FeeStructure> feeStructureMessage = feeStructureService.deleteFeeStructureById(feeStructureId);
        return feeStructureMessage;
    }
}
