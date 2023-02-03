package com.example.HardMoneyLending.mutation.property.property_type;

import graphql.kickstart.tools.GraphQLMutationResolver;
import com.example.HardMoneyLending.dto.property.property_type.CreatePropertyTypeDTO;
import com.example.HardMoneyLending.models.property.PropertyType;
import com.example.HardMoneyLending.services.property.property_type.IPropertyTypeService;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Component
@Validated
@PreAuthorize("isAuthenticated()")
public class PropertyTypeMutationResolver implements GraphQLMutationResolver {

    private final IPropertyTypeService propertyTypeService;

    @Autowired
    public PropertyTypeMutationResolver(final IPropertyTypeService propertyTypeService) {
        this.propertyTypeService = propertyTypeService;
    }

    public Message<PropertyType> createPropertyType(CreatePropertyTypeDTO createPropertyTypeDTO) throws Exception {
        try {
            Boolean isPropertyTypeUnique = propertyTypeService.isTitleUnique(createPropertyTypeDTO.getTitle());
            if(!isPropertyTypeUnique) {
                throw new Exception("Property Type already Exists");
            }
            return propertyTypeService.createPropertyType(createPropertyTypeDTO);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
