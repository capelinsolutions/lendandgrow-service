package com.example.HardMoneyLending.mutation.property;

import graphql.kickstart.tools.GraphQLMutationResolver;
import com.example.HardMoneyLending.dto.property.CreatePropertyDTO;
import com.example.HardMoneyLending.dto.property.UpdatePropertyDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.property.Property;
import com.example.HardMoneyLending.services.property.IPropertyService;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Component
@Validated
@PreAuthorize("isAuthenticated()")
public class PropertyMutationResolver implements GraphQLMutationResolver {

    private final IPropertyService service;

    public PropertyMutationResolver(final IPropertyService service) {
        this.service = service;
    }

    public Message<Property> createProperty(CreatePropertyDTO dto) throws BadRequestException, EntityNotFoundException {
        return service.createProperty(dto);
    }

    public Message<Property> updateProperty(UpdatePropertyDTO dto) throws BadRequestException, EntityNotFoundException {
        return service.updateProperty(dto);
    }
}
