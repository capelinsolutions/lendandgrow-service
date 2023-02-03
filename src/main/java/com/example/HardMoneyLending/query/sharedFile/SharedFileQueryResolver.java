package com.example.HardMoneyLending.query.sharedFile;

import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.shared_file.SharedFile;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.shared_file.ISharedFileService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Component
@Validated
@PreAuthorize("isAuthenticated()")
public class SharedFileQueryResolver implements GraphQLQueryResolver {

    private final ISharedFileService service;

    private final IUserService userService;
    @Autowired
    public SharedFileQueryResolver(final ISharedFileService service, IUserService userService) {
        this.service = service;
        this.userService = userService;
    }

    public Message<SharedFile> getSharedFileById(String id) throws EntityNotFoundException{
        return service.getSharedFileById(id);
    }

    public ListMessage<SharedFile> getAllSharedFile() throws EntityNotFoundException{
        return service.getAllSharedFile();
    }

    public ListMessage<SharedFile> getAllSharedFileByUserEmail(String userEmail) throws EntityNotFoundException{
        return service.getAllSharedFileByUserEmail(userEmail);
    }

    public Message<SharedFile> getSharedFileByPropertyId(String propertyId) throws EntityNotFoundException{
        return service.getSharedFileByPropertyId(propertyId);
    }

    public Message<SharedFile> getSharedFileByInvestorEmailAndPropertyId(String propertyId) throws EntityNotFoundException{
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        return service.getSharedFileByInvestorEmailAndPropertyId(propertyId,user.getEmail());
    }

}
