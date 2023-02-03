package com.example.HardMoneyLending.query.property.property_type;

import graphql.kickstart.tools.GraphQLQueryResolver;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.property.PropertyType;
import com.example.HardMoneyLending.services.property.property_type.PropertyTypeService;
import com.example.HardMoneyLending.utils.ListMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Component
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAuthenticated()")
public class PropertyTypeQueryResolver implements GraphQLQueryResolver {

    private PropertyTypeService propertyTypeService;

    @Autowired
    public PropertyTypeQueryResolver(PropertyTypeService propertyTypeService) {
        this.propertyTypeService = propertyTypeService;
    }

    ListMessage<PropertyType> getAllPropertyTypes() throws EntityNotFoundException {
        ListMessage<PropertyType> propertyTypes = propertyTypeService.getAllPropertyTypes();
        return propertyTypes;
    }
}
