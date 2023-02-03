package com.example.HardMoneyLending.mutation.chat;

import com.example.HardMoneyLending.dto.chat.ChatMessageRequest;
import com.example.HardMoneyLending.exception.general_exception.BadRequestException;
import com.example.HardMoneyLending.models.chat.message.ChatMessage;
import com.example.HardMoneyLending.models.chat.message.ChatMessagePublisher;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.chat.IChatMessageService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.Message;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Component
@Validated
@PreAuthorize("isAuthenticated()")
public class ChatMessageMutationResolver implements GraphQLMutationResolver {

    private final IUserService userService;
    private final IChatMessageService chatMessageService;

    public ChatMessageMutationResolver(ChatMessagePublisher publisher, IUserService userService, IChatMessageService chatMessageService) {
        this.userService = userService;
        this.chatMessageService = chatMessageService;
    }

    public Message<ChatMessage> sendMessage(ChatMessageRequest data) throws BadRequestException {

    User user = userService.loadByUsername(getContext().getAuthentication().getName());
        log.info("Publishing message from user "+user.getUserId()+" to "+data.getRecipientId());
        return chatMessageService.createChatMessage(data,user);

    }

    public Message<String> markAllUnreadMessageToRead(String senderId) {
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        chatMessageService.markAllUnreadMessageToRead(user.getUserId(), senderId);
        return new Message<String>()
                .setData("Notifications removed")
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Notifications removed");
    }

}
