package com.example.HardMoneyLending.query.chat;

import com.example.HardMoneyLending.dto.chat.PaginatedChatRequest;
import com.example.HardMoneyLending.dto.chat.PaginatedChatResponse;
import com.example.HardMoneyLending.dto.chat.notification.MessageNotification;
import com.example.HardMoneyLending.dto.chat.notification.MessageNotificationResponse;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.chat.IChatMessageService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Component
@Validated
@PreAuthorize("isAuthenticated()")
public class ChatMessageQueryResolver implements GraphQLQueryResolver {
    private final IChatMessageService chatMessageService;
    private final IUserService userService;

    public ChatMessageQueryResolver(IChatMessageService chatMessageService, IUserService userService) {
        this.chatMessageService = chatMessageService;
        this.userService = userService;
    }

    public Message<PaginatedChatResponse> getMessagesByRecipientIdAndUserIdByPagination(PaginatedChatRequest dto){
        Pageable pageable = PageRequest.of(dto.getPageNo(), dto.getPageSize(), Sort.by("sendAt").descending());
        return chatMessageService.getMessagesByRecipientIdAndUserIdByPagination(dto,pageable);
    }

    public Message<MessageNotificationResponse> getAllNotifications() throws EntityNotFoundException {
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        return chatMessageService.getAllUnreadMessages(user.getUserId());
    }
}
