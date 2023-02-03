package com.example.HardMoneyLending.repositories.property.property_type;

import com.example.HardMoneyLending.models.property.PropertyType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PropertyTypeRepository extends MongoRepository<PropertyType, String> {

    Optional<PropertyType> findByTitle(String title);

    boolean existsByPropertyTypeId(String propertyTypeId);

    List<PropertyType> findAllByPropertyTypeIdIn(List<String> ids);
}
