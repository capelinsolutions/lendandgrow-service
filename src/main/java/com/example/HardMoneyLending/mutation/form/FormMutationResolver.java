package com.example.HardMoneyLending.mutation.form;

import graphql.kickstart.tools.GraphQLMutationResolver;
import com.example.HardMoneyLending.dto.form.CreateFormDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.form.Form;
import com.example.HardMoneyLending.controller.form.IFromService;
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
public class FormMutationResolver  implements GraphQLMutationResolver {

    private final IFromService service;

    @Autowired
    public FormMutationResolver(final IFromService service) {
        this.service = service;
    }

    Message<Form> createForm(CreateFormDTO createFormDTO) throws BadRequestException, EntityNotFoundException {
        return service.createForm(createFormDTO);
    }
}
