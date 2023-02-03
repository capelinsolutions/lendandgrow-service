package com.example.HardMoneyLending.controller.form;

import com.example.HardMoneyLending.dto.form.CreateFormDTO;
import com.example.HardMoneyLending.dto.form.GetFormDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.investor.Investor;
import com.example.HardMoneyLending.models.property.PropertyType;
import com.example.HardMoneyLending.models.form.Form;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.repositories.form.FormRepository;
import com.example.HardMoneyLending.services.investor.IInvestorService;
import com.example.HardMoneyLending.services.property.property_type.IPropertyTypeService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import com.example.HardMoneyLending.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FormService implements IFromService{

    private final FormRepository repo;
    private final IPropertyTypeService propertyTypeService;

    private final IInvestorService iInvestorService;

    @Autowired
    public FormService(final FormRepository repo, final IPropertyTypeService propertyTypeService, IUserService userService, IInvestorService iInvestorService) {
        this.repo = repo;
        this.propertyTypeService = propertyTypeService;
        this.iInvestorService = iInvestorService;
    }

    @Override
    public Message<Form> createForm(CreateFormDTO createFormDTO) throws BadRequestException, EntityNotFoundException {
        PropertyType propertyType = propertyTypeService.findById(createFormDTO.getPropertyTypeId());
        Form form = new Form();
        try {
            form.setLabel(createFormDTO.getLabel());
            form.setName(createFormDTO.getName());
            form.setInputType(createFormDTO.getInputType());
            form.setPropertyTypeId(propertyType.getPropertyTypeId());
            form.setValidations(createFormDTO.getValidations());
            form.setType(createFormDTO.getType());
            form.setOptions(createFormDTO.getOptions());
            form.setValue(createFormDTO.getValue());
            form.setSelected(false);
            form = repo.save(form);

            return new Message<Form>()
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString())
                    .setMessage("Form created successfully.")
                    .setData(form);
        } catch (Exception e) {
            log.error("Error: "+e.getLocalizedMessage());
            log.error("Form can not be created.");
            throw new BadRequestException("Form can not be created.");
        }
    }

    @Override
    public ListMessage<Form> getAllForm() throws EntityNotFoundException {
        log.info("Fetching all forms.");
        List<Form> forms = repo.findAll();
        if (forms == null || forms.isEmpty()) {
            log.error("No forms found.");
            throw new EntityNotFoundException("No forms found.");
        }

        return new ListMessage<Form>()
                .setCode(HttpStatus.OK.toString())
                .setStatus(HttpStatus.OK.value())
                .setMessage("Forms are fetched successful.")
                .setData(forms);
    }

    @Override
    public ListMessage<Form> getAllFormByPropertyTypeId(String propertyTypeId) throws EntityNotFoundException {
        List<Form> forms = repo.findByPropertyTypeId(propertyTypeId);
        if (forms == null || forms.isEmpty()) {
            log.error("No form found by property type id: "+propertyTypeId);
            throw new EntityNotFoundException("No form found by provided property type id.");
        }

        return new ListMessage<Form>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("All forms by propertyTypeId fetched successfully.")
                .setData(forms);
    }

    @Override
    public ListMessage<GetFormDTO> getAllFormByListOfId(User user) throws EntityNotFoundException {
        // fetch investor by using user id
        Investor investor = iInvestorService.getInvestorByUserId(user.getUserId());
        // get all questions list
        List<String> questions = investor.getQuestions();

        if(questions!=null){
            Set<Form> formList = repo.findAllFormByIdIn(questions);

            if (formList == null || formList.isEmpty()) {
                log.error("No form found.");
                throw new com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException("No form found.");
            }

            // update forms list attribute value=true
            formList.stream().forEach(e -> e.setSelected(true));

            ListMessage<Form> allFormList = getAllForm();

            // merging form lists

            for (Form form : allFormList.getData()){
                formList.add(form);
            }

            return allFormsMergeWithProperty(formList);

        }

        else {
            // fetch all the forms
            ListMessage<Form> allFormList = getAllForm();
            return allFormsMergeWithProperty(new HashSet<>(allFormList.getData()));

        }

    }

    private ListMessage<GetFormDTO> allFormsMergeWithProperty(Set<Form> formList){

        // find distinct propertyTypeId
        List<String> propertyTypeId = formList.stream()
                .filter(StreamUtils.distinctByKey(p -> p.getPropertyTypeId()))
                .map(e -> new String(e.getPropertyTypeId()))
                .collect(Collectors.toList());

        // Fetch all PropertyType by list of propertyTypeId
        List<PropertyType> listOfPropertyType = propertyTypeService.getAllByListOfPropertyTypeId(propertyTypeId);

        // Check whether we have a single propertyTypeId
        // If present then simply put the whole formList into our response object (GetFormDTO) with
        // propertyTypeId
        List<GetFormDTO> responseData = new ArrayList<>();
        if (propertyTypeId.size() == 1) {
            responseData.add(new GetFormDTO(listOfPropertyType.get(0), formList));
        } else{
            listOfPropertyType.stream().forEach(e -> {
                responseData.add(new GetFormDTO(e, formList.stream()
                        .filter(a -> a.getPropertyTypeId().equals(e.getPropertyTypeId()))
                        .collect(Collectors.toSet())));
            });
        }

        if (responseData == null || responseData.isEmpty()) {
            throw new com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException("No data found.");
        }

        return new ListMessage<GetFormDTO>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Form fetched successfully.")
                .setData(responseData);
    }

    @Override
    public Form getFormByName(String documentTypeName) throws com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException {
        Form form = repo.findByName(documentTypeName);
        if (form == null) {
            log.info("Form not found with name: "+documentTypeName);
            throw new com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException("Form not found with name: "+documentTypeName);
        }
        return form;
    }

    @Override
    public ListMessage<GetFormDTO> getFormsByListOfForms(List<String> listOfFormsIds) throws EntityNotFoundException{

        log.info("Fetching forms of given form Ids.");

        if(listOfFormsIds==null || listOfFormsIds.isEmpty()){
            log.info("Form Ids list is null or empty.");
            throw new EntityNotFoundException("Form Ids list is null or empty.");
        }

        // Get all propertyType i.e 3
        ListMessage<PropertyType> propertyTypeListMessage = propertyTypeService.getAllPropertyTypes();
        if(propertyTypeListMessage==null ){
            log.info("No property Type found in Database.");
            throw new EntityNotFoundException("No property Type found in Database.");
        }
        List<PropertyType> propertyTypes = propertyTypeListMessage.getData();

        // Get all forms by propertyTypeId i.e findAllByIdInAndPropertyTypeId(): List<Form>
        // lenderFormResponseList.add(new LenderFormResponseDTO -> set proppeertyType and form

        List<GetFormDTO> getFormList = new ArrayList<>();

        for(PropertyType propertyType : propertyTypes){
            Set<Form> forms = repo.findAllByIdInAndPropertyTypeId(listOfFormsIds,propertyType.getPropertyTypeId());
            if(forms!=null && !forms.isEmpty()){
                getFormList
                        .add(
                                GetFormDTO
                                        .builder()
                                        .propertyType(propertyType)
                                        .forms(forms)
                                        .build()
                        );
            }
        }

        return new ListMessage<GetFormDTO>()
                .setCode(HttpStatus.OK.toString())
                .setStatus(HttpStatus.OK.value())
                .setMessage("Forms are fetched successful.")
                .setData(getFormList);

    }

}
