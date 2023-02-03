package com.example.HardMoneyLending.query.form;

import com.example.HardMoneyLending.dto.form.GetFormDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.user.IUserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import com.example.HardMoneyLending.models.form.Form;
import com.example.HardMoneyLending.controller.form.IFromService;
import com.example.HardMoneyLending.utils.ListMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Component
@Validated
@PreAuthorize("isAuthenticated()")
public class FormQueryResolver implements GraphQLQueryResolver {

    private final IFromService service;

    private final IUserService userService;

    @Autowired
    public FormQueryResolver(final IFromService service, IUserService userService) {
        this.service = service;
        this.userService = userService;
    }

    public ListMessage<Form> getAllForm() throws EntityNotFoundException {
        return service.getAllForm();
    }

    public ListMessage<Form> getAllFormByPropertyTypeId(String propertyTypeId) throws EntityNotFoundException{
        return service.getAllFormByPropertyTypeId(propertyTypeId);
    }

    public ListMessage<GetFormDTO> getAllFormByListOfId() throws EntityNotFoundException {
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        return service.getAllFormByListOfId(user);
    }

    public ListMessage<GetFormDTO> getFormsByListOfForms(List<String> formList) throws EntityNotFoundException {
        return service.getFormsByListOfForms(formList);
    }
}
