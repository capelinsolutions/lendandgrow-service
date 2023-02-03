package com.example.HardMoneyLending.controller.form;

import com.example.HardMoneyLending.dto.form.CreateFormDTO;
import com.example.HardMoneyLending.dto.form.GetFormDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.form.Form;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;


import java.util.List;

public interface IFromService {
    Message<Form> createForm(CreateFormDTO createFormDTO) throws BadRequestException, EntityNotFoundException;

    ListMessage<Form> getAllForm() throws EntityNotFoundException;

    ListMessage<Form> getAllFormByPropertyTypeId(String propertyTypeId) throws EntityNotFoundException;

    ListMessage<GetFormDTO> getAllFormByListOfId(User user) throws EntityNotFoundException;

    Form getFormByName(String documentTypeName) throws com.example.HardMoneyLending.exception.general_exception.EntityNotFoundException;

    ListMessage<GetFormDTO> getFormsByListOfForms(List<String> listOfFormsIds) throws EntityNotFoundException;
}
