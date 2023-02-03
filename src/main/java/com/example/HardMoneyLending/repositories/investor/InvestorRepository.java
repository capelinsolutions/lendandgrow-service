package com.example.HardMoneyLending.repositories.investor;

import com.example.HardMoneyLending.models.investor.Investor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestorRepository extends MongoRepository<Investor, String> {

    List<Investor> findFirst3ByOrderByCreatedAtDesc();

    Page<Investor> findAllByNameContainingIgnoreCaseOrStateContainingIgnoreCaseOrCityContainingIgnoreCaseOrFeeStructureId(
            @Param("name") String name,
            @Param("state") String state,
            @Param("city") String city,
            @Param("feeStructureId") String feeStructureId, Pageable pageable);

    // findAllByNameContainingIgnoreCaseOrStateContainingIgnoreCaseOrCityContainingIgnoreCaseOrFeeStructureId
    Investor findByUserId(String userId);

    List<Investor> findAllByIsActive(boolean isActive);

    long countByUserIdIn(List<String> recipientIdList);
}
