package com.example.HardMoneyLending.services.property.property_type;

import com.example.HardMoneyLending.dto.property.property_type.CreatePropertyTypeDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.property.PropertyType;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;

import java.util.List;

public interface IPropertyTypeService {
    Boolean isTitleUnique(String title);

    //    Mutation
    Message createPropertyType(CreatePropertyTypeDTO createPropertyTypeDTO);
    ListMessage<PropertyType> getAllPropertyTypes() throws EntityNotFoundException;

    PropertyType findById(String propertyTypeId) throws EntityNotFoundException, BadRequestException;

    boolean isPropertyTypeExists(String propertyTypeId) throws BadRequestException, EntityNotFoundException;

    void save(PropertyType propertyType) throws BadRequestException;

    List<PropertyType> getAllByListOfPropertyTypeId(List<String> ids) throws com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
}
