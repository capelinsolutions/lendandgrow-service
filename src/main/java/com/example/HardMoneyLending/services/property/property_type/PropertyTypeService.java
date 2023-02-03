package com.example.HardMoneyLending.services.property.property_type;

import com.example.HardMoneyLending.dto.property.property_type.CreatePropertyTypeDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.property.PropertyType;
import com.example.HardMoneyLending.repositories.property.property_type.PropertyTypeRepository;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PropertyTypeService implements IPropertyTypeService {

    private final PropertyTypeRepository propertyTypeRepository;

    @Autowired
    public PropertyTypeService(final PropertyTypeRepository propertyTypeRepository) {
        this.propertyTypeRepository = propertyTypeRepository;
    }

    @Override
    public Boolean isTitleUnique(String title) {
        return (!propertyTypeRepository.findByTitle(title).isPresent());
    }

    @Override
    public Message<PropertyType> createPropertyType(CreatePropertyTypeDTO createPropertyTypeDTO) {
        try {
            PropertyType propertyType = new PropertyType();
            propertyType.setTitle(createPropertyTypeDTO.getTitle());
            propertyType.setDescription(createPropertyTypeDTO.getDescription());
            propertyType.setIconUrl(createPropertyTypeDTO.getIconUrl());
            propertyType = propertyTypeRepository.save(propertyType);
            return new Message<PropertyType>().setStatus(200).setMessage("Property Type Created Successfully").setData(propertyType);

        } catch (Exception e) {
            return new Message<PropertyType>().setMessage(e.getMessage()).setStatus(400);
        }
    }

    @Override
    public ListMessage<PropertyType> getAllPropertyTypes() throws EntityNotFoundException {
        List<PropertyType> propertyTypes = propertyTypeRepository.findAll();
        if (propertyTypes == null || propertyTypes.isEmpty()) {
            log.error("No property types found");
            throw new EntityNotFoundException("No property types found.");
        }

        return new ListMessage<PropertyType>().setMessage("Property types fetched successfully.")
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setData(propertyTypes);
    }

    @Override
    public PropertyType findById(String propertyTypeId) throws EntityNotFoundException, BadRequestException {
        if (propertyTypeId == null || propertyTypeId.isEmpty()) {
            log.error("Property type should not be null");
            throw new BadRequestException("Property type should not be null");
        }

        Optional<PropertyType> propertyType = propertyTypeRepository.findById(propertyTypeId);
        if (!propertyType.isPresent()) {
            log.error("Property type is not found with id: "+propertyTypeId);
            throw new EntityNotFoundException("No property found with provided id.");
        }

        return propertyType.get();
    }

    @Override
    public boolean isPropertyTypeExists(String propertyTypeId) throws BadRequestException, EntityNotFoundException {
        if (propertyTypeId == null || propertyTypeId.isEmpty()) {
            log.error("Property type id: "+propertyTypeId+" should not be null or empty.");
            throw new BadRequestException("Provided property type id should not be null or empty.");
        }

        boolean isExists = propertyTypeRepository.existsByPropertyTypeId(propertyTypeId);
        if (!isExists) {
            log.error("Property type is not found with id: "+propertyTypeId);
            throw new EntityNotFoundException("Property type is not found with provided id.");
        }

        return isExists;
    }

    @Override
    public void save(PropertyType propertyType) throws BadRequestException {
        try {
            propertyTypeRepository.save(propertyType);
            log.debug("Property type is saved successfully");
        } catch (Exception e) {
            log.error("There is some error on backend side.");
            throw new BadRequestException("There is some error on backend side.");
        }
    }

    @Override
    public List<PropertyType> getAllByListOfPropertyTypeId(List<String> ids) throws com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException {
        List<PropertyType> propertyTypes = propertyTypeRepository.findAllByPropertyTypeIdIn(ids);
        if (propertyTypes == null || propertyTypes.isEmpty()) {
            throw new com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException("No property type record found by ids.");
        }
        return propertyTypes;
    }
}
