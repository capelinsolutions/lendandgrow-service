package com.example.HardMoneyLending.subscription.chat;

import com.example.HardMoneyLending.models.chat.message.ChatMessage;
import com.example.HardMoneyLending.models.chat.message.ChatMessagePublisher;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.chat.ChatMessageService;
import com.example.HardMoneyLending.services.user.IUserService;
import graphql.kickstart.servlet.context.GraphQLWebSocketContext;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Optional;

@Slf4j
@Component
public class ChatMessageSubscriptionResolver implements GraphQLSubscriptionResolver {

    private final ChatMessagePublisher chatMessagePublisher;
    private final IUserService userService;
    private final ChatMessageService chatMessageService;

    public ChatMessageSubscriptionResolver(final ChatMessagePublisher chatMessagePublisher, IUserService userService, ChatMessageService chatMessageService) {
        this.chatMessagePublisher = chatMessagePublisher;
        this.userService = userService;
        this.chatMessageService = chatMessageService;
    }

    @PreAuthorize("hasAuthority('get:chat_message')")
    public Publisher<ChatMessage> getChatMessages(DataFetchingEnvironment env) {
        GraphQLWebSocketContext context = env.getContext();
        Optional<Authentication> authentication =
                Optional.ofNullable(context.getSession())
                        .map(Session::getUserProperties)
                        .map(props -> props.get("CONNECT_TOKEN"))
                        .map(Authentication.class::cast);
        log.info("Subscribe to publisher with token: {}", authentication);
        authentication.ifPresent(SecurityContextHolder.getContext()::setAuthentication);
        log.info(
                "Security context principal: {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return chatMessagePublisher.getChatMessagePublisher();
    }

    @PreAuthorize("hasAuthority('get:chat_message')")
    public Publisher<ChatMessage> getChatMessageData(String senderId, DataFetchingEnvironment env) {
        GraphQLWebSocketContext context = env.getContext();
        Optional<Authentication> authentication =
                Optional.ofNullable(context.getSession())
                        .map(Session::getUserProperties)
                        .map(props -> props.get("CONNECT_TOKEN"))
                        .map(Authentication.class::cast);
        log.info("Subscribe to publisher with token: {}", authentication);
        authentication.ifPresent(SecurityContextHolder.getContext()::setAuthentication);
        log.info(
                "Security context principal: {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        User user = userService.getUserByUserId((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        log.info("Sender ID: "+senderId+", Recipient ID: "+user.getUserId());
        return chatMessagePublisher.getChatMessagePublisherFor(user.getUserId(), senderId);
    }
}
