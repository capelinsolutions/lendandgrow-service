package com.example.HardMoneyLending.services.property;

import com.example.HardMoneyLending.dto.property.*;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.models.form.Form;
import com.example.HardMoneyLending.models.property.Property;
import com.example.HardMoneyLending.models.property.PropertyType;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.repositories.property.PropertyRepository;
import com.example.HardMoneyLending.services.borrower.IBorrowerService;
import com.example.HardMoneyLending.controller.form.IFromService;
import com.example.HardMoneyLending.services.image.IImageService;
import com.example.HardMoneyLending.services.property.property_type.PropertyTypeService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Service
public class PropertyService implements IPropertyService{

    private static final String QUESTION_INPUT_TYPE_FOR_FILE = "file";

    private final PropertyRepository repo;
    private final PropertyTypeService propertyTypeService;
    private final IBorrowerService borrowerService;
    private final IFromService formService;
    private final IImageService imageService;
    private final IUserService userService;

    @Autowired
    public PropertyService(final PropertyRepository repo,
                           final PropertyTypeService propertyTypeService,
                           final IBorrowerService borrowerService,
                           final IFromService formService,
                           final IImageService imageService, IUserService userService) {
        this.repo = repo;
        this.propertyTypeService = propertyTypeService;
        this.borrowerService = borrowerService;
        this.formService = formService;
        this.imageService = imageService;
        this.userService = userService;
    }

    @Override
    public Message<Property> createProperty(CreatePropertyDTO dto) throws EntityNotFoundException, BadRequestException {
        log.info("Creating property in our database.");

        // Check whether borrower and property type exists
        PropertyType propertyType = propertyTypeService.findById(dto.getPropertyTypeId());

        Borrower borrower = borrowerService.getBorrower(dto.getBorrowerId());

        // Check if all propertyTypeId are the same in the form/question list
        if (dto.getQuestions().stream().filter(e ->
                        e.getPropertyTypeId().equals(dto.getPropertyTypeId()))
                .collect(Collectors.toList()).size() != dto.getQuestions().size()) {
            throw new BadRequestException("All questions should have same property type as the property.");
        }

        Property property = new Property(
                null,
                dto.getBorrowerId(),
                dto.getPropertyTypeId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getShowPrice(),
                dto.getImageUrls(),
                dto.getAddress(),
                dto.getArea(),
                dto.getQuestions(),
                System.currentTimeMillis(),
                null,
                Boolean.TRUE
        );

        property = repo.save(property);

        // Create property type data in borrower
        if (borrower.getPropertyType() == null) {
            Set<PropertyType> propertyTypes = new HashSet<>();
            propertyTypes.add(propertyType);
            borrower.setPropertyType(propertyTypes);
        } else {
            borrower.getPropertyType().add(propertyType);
            borrower.setPropertyType(borrower.getPropertyType());
        }

        borrowerService.save(borrower);

        return new Message<Property>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Property is saved successfully.")
                .setData(property);
    }

    @Override
    public Property save(Property property) throws BadRequestException {
        try {
            return repo.save(property);
        } catch (Exception e) {
            log.error("Error: "+e.getLocalizedMessage());
            throw new BadRequestException("Some thing went wrong at backend side.");
        }
    }

    @Override
    public Message<Property> updateProperty(UpdatePropertyDTO dto) throws EntityNotFoundException, BadRequestException {

        Optional<Property> savedProperty = repo.findByPropertyIdAndActive(dto.getPropertyId(), true);
        if (savedProperty == null || !savedProperty.isPresent()) {
            log.error("Property not found with id: "+dto.getPropertyId());
            throw new EntityNotFoundException("Property not found with provided id.");
        }

        // Check whether property type id not null or empty or not found
        propertyTypeService.isPropertyTypeExists(dto.getPropertyTypeId());

        // Check whether borrower id not null or empty or not found
        borrowerService.isBorrowerExists(dto.getBorrowerId());

        // Check if all propertyTypeId are the same in the form/question list
        if (dto.getQuestions().stream().filter(e ->
                        e.getPropertyTypeId().equals(dto.getPropertyTypeId()))
                .collect(Collectors.toList()).size() != dto.getQuestions().size()) {
            throw new BadRequestException("All questions should have same property type as the property.");
        }

        savedProperty.get().setPropertyTypeId(dto.getPropertyTypeId());
        savedProperty.get().setTitle(dto.getTitle());
        savedProperty.get().setDescription(
                dto.getDescription() != null ?
                        dto.getDescription() : savedProperty.get().getDescription()
        );
        savedProperty.get().setPrice(dto.getPrice());
        savedProperty.get().setShowPrice(dto.getShowPrice());
        savedProperty.get().setImageUrls(dto.getImageUrls());
        savedProperty.get().setAddress(dto.getAddress());
        savedProperty.get().setArea(dto.getArea());
        savedProperty.get().setQuestions(dto.getQuestions());
        savedProperty.get().setBorrowerId(dto.getBorrowerId());
        savedProperty.get().setUpdatedAt(System.currentTimeMillis());
        savedProperty.get().setActive(dto.getActive());

        try {
            repo.save(savedProperty.get());
            log.info("Property updated successfully with property id: " + savedProperty.get().getPropertyId());
            return new Message<Property>()
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString())
                    .setMessage("Property updated successfully.")
                    .setData(savedProperty.get());
        } catch (Exception e) {
            log.error("Error: "+ e.getLocalizedMessage());
            throw new BadRequestException("Internal database issue occurred.");
        }
    }

    @Override
    public boolean isPropertyExistsById(String propertyId) throws BadRequestException, EntityNotFoundException {
        if (propertyId == null || propertyId.isEmpty()) {
            log.error("Provided property id: "+propertyId+" should not be null.");
            throw new BadRequestException("Property id should not be null or empty.");
        }

        boolean isExist = repo.existsById(propertyId);
        if (!isExist) {
            log.error("Property not found with id: "+propertyId);
            throw new EntityNotFoundException("Property not found with provided id.");
        }

        return isExist;
    }

    @Override
    public Message<Property> getPropertyById(String id, boolean isActive) throws EntityNotFoundException {
        Optional<Property> property = repo.findByPropertyIdAndActive(id, isActive);
        if (property == null || !property.isPresent()) {
            log.error("Property not found with id: "+id);
            throw new EntityNotFoundException("Property not found.");
        }

        return new Message<Property>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Property fetched successfully.")
                .setData(property.get());
    }

    @Override
    public Message<Property> getPropertyByIdForUpdate(String id, boolean isActive) throws EntityNotFoundException {
        // First get property by id
        Optional<Property> savedProperty = repo.findByPropertyIdAndActive(id, isActive);

        if (savedProperty == null || !savedProperty.isPresent()) {
            log.error("Property not found with id: "+id);
            throw new EntityNotFoundException("Property not found with provided id.");
        }

        // Get all form by property type id
        List<Form> allForms = Message.getFromListMessage(
                formService.getAllFormByPropertyTypeId(
                        savedProperty.get().getPropertyTypeId()
                )
        );

        // Merge saved questions and unsaved questions
        Set<Form> allFormsForResponse = new HashSet<>();
        allFormsForResponse.addAll(allForms);
        allFormsForResponse.addAll(savedProperty.get().getQuestions());

        savedProperty.get().setQuestions(new ArrayList<>(allFormsForResponse));

        return new Message<Property>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Property fetched successfully.")
                .setData(savedProperty.get());
    }

    @Override
    public ListMessage<Property> getAllProperty(boolean isActive) throws EntityNotFoundException {
        List<Property> data = repo.findAllByActive(isActive);
        if (data == null || data.isEmpty()) {
            log.error("No property found.");
            throw new EntityNotFoundException("No property found.");
        }

        return new ListMessage<Property>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Property successfully fetched.")
                .setData(data);
    }

    @Override
    public ListMessage<Property> getAllPropertyByBorrowerId(String borrowerId, boolean isActive) throws EntityNotFoundException {
        List<Property> data = repo.findAllByBorrowerIdAndActive(borrowerId, isActive);
        if (data == null || data.isEmpty()) {
            log.error("No property found.");
            throw new EntityNotFoundException("Property not found.");
        }

        return new ListMessage<Property>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Successfully fetched properties.")
                .setData(data);

    }

    @Override
    public ListMessage<Property> getAllPropertyByPropertyTypeId(String propertyTypeId, boolean isActive) throws EntityNotFoundException {
        List<Property> data = repo.findAllByPropertyTypeIdAndActive(propertyTypeId, isActive);
        if (data == null || data.isEmpty()) {
            log.error("No property found.");
            throw new EntityNotFoundException("No property found.");
        }

        return new ListMessage<Property>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Property fetched successfully.")
                .setData(data);
    }

    @Override
    public List<String> storeImage(MultipartFile[] multipartFiles, String fileType, String propertyId)
            throws com.example.HardMoneyLending.exception.general_exception.BadRequestException,
            com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException {
        if (fileType == null || fileType.isEmpty() || fileType.equalsIgnoreCase("null")) {
            log.error("File type should not be null or empty.");
            throw new com.example.HardMoneyLending.exception.general_exception.BadRequestException("File type should not be null or empty.");
        }

        List<String> urlList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            // check if it contains any special characters except ' " "
            if (Pattern.compile("[^0-9A-Za-z' ._-]").matcher(file.getOriginalFilename()).find()) {
                log.info("Please remove special character from the file name (i.e Simple Name.pdf can have . '-_ special characters).");
                throw new com.example.HardMoneyLending.exception.general_exception.BadRequestException("Please remove special character from the file name (i.e Simple Name.pdf can have . '-_ special characters).");
            }
        }

        for (MultipartFile file : multipartFiles) {
            urlList.add(imageService.processImage(file, fileType));
        }

        // check if property id is provided then do following
        if (propertyId != null && !propertyId.isEmpty()) {
            updatePropertyWithNewDocument(propertyId, urlList);
        }

        return urlList;
    }

    // TODO property upload
    private void updatePropertyWithNewDocument(String propertyId, List<String> urlList) throws com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException {
        Property property = null;
        try {
            property = getPropertyById(propertyId, true).getData();
        } catch (EntityNotFoundException exception) {
            throw new com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException("Property not found with property id: "+propertyId);
        }
        List<Form> forms = property.getQuestions();
        // Check if the property has document type form then add all document's urls in the value
        Optional<Form> form = forms.stream().filter(e -> e.getName().equalsIgnoreCase(Form.DOCUMENT_TYPE_NAME)).findAny();
        if (form.isPresent()) {
            String values = form.get().getValue();
            if (values!=null && ( values.equals("null") || values.isEmpty() ) ){
                form.get().setValue(String.join("", urlList));
            }else{
                form.get().setValue(values + "," + String.join(",", urlList));
            }
            forms.remove(form.get());
            forms.add(form.get());
        } else {
            Form getForm = formService.getFormByName(Form.DOCUMENT_TYPE_NAME);
            getForm.setValue(String.join(",", urlList));
            forms.add(getForm);

//            String values = form.get().getValue();
//            Form getForm = formService.getFormByName(Form.DOCUMENT_TYPE_NAME);
//            if (values!=null && ( values.equals("null") || values.isEmpty() ) ){
//                //form.get().setValue(String.join("", urlList));
//                getForm.setValue(String.join(",", urlList));
//            }else{
//                //form.get().setValue(values + "," + String.join(",", urlList));
//                getForm.setValue(values + "," + String.join(",", urlList));
//            }
//            getForm.setValue(String.join(",", urlList));
//            forms.add(getForm);

        }
        repo.save(property);
    }

    @Override
    public ListMessage getAllPropertyForLoggedInUser(User user) throws EntityNotFoundException {
        Borrower borrower = borrowerService.getBorrowerByUserId(user.getUserId());
        return getAllPropertyByBorrowerId(borrower.getBorrowerId(), true);
    }

    @Override
    public Message<PaginatedPropertyResponse> getAllPropertyByPaginationWithFilter(PropertyFilterDTO request, Pageable pageable) throws EntityNotFoundException {

        Page<Property> resulting = null;
        if (request.getCity() == null &&
                request.getAddress() == null &&
                request.getPriceStart() == null &&
                request.getPriceEnd() == null &&
                request.getPropertyTypeTitle() == null) {
            resulting = repo.findAll(pageable);
        } else {
            // JUGAAR: Spring boot jpa issue for mongodb that if we provide null with ContainingIgnoreCase query
            // then it will throw IllegalArgument exception, so we have to use any special character for instead
            // null

            populateSpecialCharWhenNullFilterValues(request);
            resulting = repo.
                    findAllByAreaContainingIgnoreCaseOrAddressContainingIgnoreCaseOrPropertyTypeIdContainingOrPriceBetween(
                            request.getCity(), request.getAddress(), request.getPropertyTypeTitle(), request.getPriceStart(), request.getPriceEnd(), pageable);
        }

        if (resulting.getContent() == null || resulting.getContent().isEmpty())
            throw new EntityNotFoundException("No data found.");

        PaginatedPropertyResponse response = PaginatedPropertyResponse.builder()
                .property(resulting.getContent())
                .pageNo(request.getPageNo())
                .pageSize(request.getPageSize())
                .noOfPages(resulting.getTotalPages())
                .totalRows(resulting.getTotalElements())
                .build();

        return new Message<PaginatedPropertyResponse>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Successfully Fetched Properties With Filter.")
                .setData(response);

    }

    private void populateSpecialCharWhenNullFilterValues(PropertyFilterDTO request) {
        request.setCity(request.getCity()== null ? "@" : request.getCity() );
        request.setAddress(request.getAddress() == null ? "@" : request.getAddress() );
        request.setPropertyTypeTitle(request.getPropertyTypeTitle() == null ? "@" : request.getPropertyTypeTitle() );
    }


    @Override
    public Message<PaginatedPropertyResponse> getAllPropertyByPagination(PaginatedPropertyRequest request, User user, Pageable pageable) throws EntityNotFoundException {
        Borrower borrower = borrowerService.getBorrowerByUserId(user.getUserId());

        Page<Property> resulting = null;
        if (request.getArea() == null && request.getTitle() == null && request.getPriceStart() == null && request.getPriceEnd() == null) {
            resulting = repo.findAllByBorrowerId(borrower.getBorrowerId(), pageable);
        } else {
            // JUGAAR: Spring boot jpa issue for mongodb that if we provide null with ContainingIgnoreCase query
            // then it will throw IllegalArgument exception so we have to use any special character for instead
            // null

            populateSpecialCharWhenProvidedNullFilterValues(request);
            resulting = repo.
                    findAllByBorrowerIdAndTitleContainingIgnoreCaseOrAreaContainingIgnoreCaseOrPriceBetween(
                            borrower.getBorrowerId(), request.getTitle(), request.getArea(), request.getPriceStart(), request.getPriceEnd(), pageable);
            }

            if (resulting.getContent() == null || resulting.getContent().isEmpty())
                throw new EntityNotFoundException("No data found.");

            PaginatedPropertyResponse response = PaginatedPropertyResponse.builder()
                    .property(resulting.getContent())
                    .pageNo(request.getPageNo())
                    .pageSize(request.getPageSize())
                    .noOfPages(resulting.getTotalPages())
                    .totalRows(resulting.getTotalElements())
                    .build();

            return new Message<PaginatedPropertyResponse>()
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString())
                    .setMessage("Successfully Fetched.")
                    .setData(response);

    }

    private void populateSpecialCharWhenProvidedNullFilterValues(PaginatedPropertyRequest request) {
        request.setTitle(request.getTitle()== null ? "@" : request.getTitle() );
        request.setArea(request.getArea() == null ? "@" : request.getArea() );
    }

    @Override
    public boolean isExistsInProperty(String username, String url) throws com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException, MalformedURLException {
        // First check is it borrower
        User user = userService.loadByUsername(username.trim());
        Borrower borrower = null;
        try {
            borrower = borrowerService.getBorrowerByUserId(user.getUserId());
        } catch (Exception e) {
            return false;
        }

        List<Property> property = repo.findAllByBorrowerIdAndActive(borrower.getBorrowerId(), true);
        if(property == null || property.isEmpty())
            throw new com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException("This borrower doesn't have any property.");

        List<Form> questions = new ArrayList<>();
        property.stream().forEach(e -> questions.addAll(e.getQuestions()));

        Optional<Form> form = questions.stream()
                .filter(e -> e.getName().equals(Form.DOCUMENT_TYPE_NAME) && e.getValue().contains(url))
                .findFirst();
        if(form == null || !form.isPresent())
            return false;
        return true;
    }

    @Override
    public Message<Integer> getTotalNoOfPropertiesOfBorrower(String borrowerId) throws EntityNotFoundException{
        log.info("Getting the total no of contacted investors with user id: "+borrowerId);

        if(borrowerId==null || borrowerId.isEmpty()){
            log.error("Borrower Id should not be null or empty. ");
            throw new EntityNotFoundException("Borrower Id should not be null or empty.");
        }
        long count = countPropertiesOfBorrower(borrowerId);

        return new Message<Integer>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Property fetched successfully.")
                .setData(Integer.parseInt(""+count));
    }

    private long countPropertiesOfBorrower(String borrowerId){
        return repo.countByBorrowerIdAndActive(borrowerId,true);
    }
}


