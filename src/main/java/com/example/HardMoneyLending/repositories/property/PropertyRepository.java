package com.example.HardMoneyLending.repositories.property;

import com.example.HardMoneyLending.models.property.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends MongoRepository<Property, String> {

    List<Property> findAllByBorrowerIdAndActive(String borrowerId, boolean isActive);

    List<Property> findAllByPropertyTypeIdAndActive(String propertyTypeId, boolean isActive);

    Optional<Property> findByPropertyIdAndActive(String id, boolean isActive);

    List<Property> findAllByActive(boolean isActive);

    Page<Property> findAllByBorrowerId(String borrowerId, Pageable pageable);

    Page<Property> findAllByBorrowerIdAndTitleContainingIgnoreCaseOrAreaContainingIgnoreCaseOrPriceBetween(String borrowerId, String title, String area, Double priceStart, Double priceEnd, Pageable pageable);

    long countByBorrowerIdAndActive(String borrowerId, boolean isActive);

    Page<Property> findAllByAreaContainingIgnoreCaseOrAddressContainingIgnoreCaseOrPropertyTypeIdContainingOrPriceBetween(String city, String address, String propertyTypeTitle, Double priceStart, Double priceEnd, Pageable pageable);
}
