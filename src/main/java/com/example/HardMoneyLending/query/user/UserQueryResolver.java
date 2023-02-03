package com.example.HardMoneyLending.query.user;

import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import graphql.kickstart.tools.GraphQLQueryResolver;
import com.example.HardMoneyLending.dto.user.user_type.UserTypeDTO;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.user.UserService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Component
@Validated
public class UserQueryResolver implements GraphQLQueryResolver {

    private UserService userService;

    @Autowired
    public UserQueryResolver(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    public  User getUser() throws Exception{
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        if (user == null) {

            throw new Exception("User not found");
        }
        return user;
    }

    public ListMessage<UserTypeDTO> getUserTypes() throws Exception{
        ListMessage<UserTypeDTO> userTypes = userService.getUserTypes();
        System.out.println(userTypes);
        if (userTypes == null) {
            throw new Exception("No user types exists");
        }
        return userTypes;
    }

    public Message<String> getProfileImageUrlByUserId(String userId) throws EntityNotFoundException{
        return userService.getUserProfileImageByUSerId(userId);
    }

    @PreAuthorize("isAuthenticated()")
    public Message<User> loadByUsername() throws Exception{
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        Message m = new Message();
        return m.setData(user).setMessage("Successfully fetched.").setStatus(HttpStatus.OK.value()).setCode(HttpStatus.OK.toString());
    }
}
