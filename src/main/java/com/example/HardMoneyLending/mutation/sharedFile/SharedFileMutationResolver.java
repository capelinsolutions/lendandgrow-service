package com.example.HardMoneyLending.mutation.sharedFile;

import com.example.HardMoneyLending.dto.shared_file.CreateSharedFileDTO;
import com.example.HardMoneyLending.dto.shared_file.UpdateSharedFileDTO;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.shared_file.SharedFile;
import com.example.HardMoneyLending.services.shared_file.ISharedFileService;
import com.example.HardMoneyLending.utils.Message;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Component
@Validated
@PreAuthorize("isAuthenticated()")
public class SharedFileMutationResolver implements GraphQLMutationResolver {

    private final ISharedFileService service;

    public SharedFileMutationResolver(ISharedFileService service) {
        this.service = service;
    }

    public Message<SharedFile> createSharedFile(CreateSharedFileDTO dto) throws BadRequestException, EntityNotFoundException{
        return service.createSharedFile(dto);
    }

    public Message<SharedFile> updateSharedFile(UpdateSharedFileDTO dto) throws BadRequestException, EntityNotFoundException{
        return service.updateSharedFile(dto);
    }
}
