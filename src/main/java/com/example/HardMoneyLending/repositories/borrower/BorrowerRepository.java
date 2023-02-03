package com.example.HardMoneyLending.repositories.borrower;

import com.example.HardMoneyLending.models.borrower.Borrower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowerRepository extends MongoRepository<Borrower, String> {

    //Page<Borrower> findAllByNameContainingIgnoreCaseOrStateContainingIgnoreCaseOrCityContainingIgnoreCaseOrPropertyType_TitleIgnoreCase
     //       (String name, String state, String city, String propertyTypeId, Pageable pageable);

    boolean existsByBorrowerId(String borrowerId);

    Optional<Borrower> findByUserId(String userId);

    long countByUserIdIn(List<String> recipientIdList);

    //Page<Borrower> findAllByNameContainingIgnoreCaseOrStateContainingIgnoreCaseOrCityContainingIgnoreCaseOrPropertyType_Id(String name, String state, String city, String propertyTypeId, Pageable pageable);

    Page<Borrower> findAllByNameContainingIgnoreCaseOrStateContainingIgnoreCaseOrCityContainingIgnoreCaseOrPropertyType_propertyTypeId(String name, String state, String city, String propertyTypeId, Pageable pageable);
}
