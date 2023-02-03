package com.example.HardMoneyLending.repositories.fee_structure;

import com.example.HardMoneyLending.models.fee_structure.FeeStructure;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FeeStructureRepository extends MongoRepository<FeeStructure, String> {

    FeeStructure findByIdAndActive(String id, boolean isActive);

    List<FeeStructure> findAllByActive(boolean b);
}
