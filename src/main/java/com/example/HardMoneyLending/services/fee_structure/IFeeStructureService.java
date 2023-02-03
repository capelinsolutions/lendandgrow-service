package com.example.HardMoneyLending.services.fee_structure;

import com.example.HardMoneyLending.dto.fee_structure.CreateFeeStructureDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotSavedException;
import com.example.HardMoneyLending.exception.general_exception_graphql.UnprocessableException;
import com.example.HardMoneyLending.models.fee_structure.FeeStructure;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;

public interface IFeeStructureService {
    Message<FeeStructure> createFeeStructure(CreateFeeStructureDTO createFeeStructureDTO) throws EntityNotSavedException;

    Message<FeeStructure> deleteFeeStructureById(String feeStructureId) throws EntityNotFoundException, UnprocessableException;

    ListMessage<FeeStructure> getAllFeeStructure();

    Message<FeeStructure> getFeeStructureById(String id);
}
