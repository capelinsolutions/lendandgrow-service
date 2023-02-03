package com.example.HardMoneyLending.subscription.chat_notification;

import com.example.HardMoneyLending.dto.chat.notification.MessageNotificationResponse;
import com.example.HardMoneyLending.models.chat.message.ChatNotificationPublisher;
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
public class ChatNotificationSubscriptionResolver implements GraphQLSubscriptionResolver {

    private final ChatNotificationPublisher chatNotificationPublisher;
    private final IUserService userService;
    private final ChatMessageService chatMessageService;

    public ChatNotificationSubscriptionResolver(ChatNotificationPublisher chatNotificationPublisher, IUserService userService, ChatMessageService chatMessageService) {
        this.chatNotificationPublisher = chatNotificationPublisher;
        this.userService = userService;
        this.chatMessageService = chatMessageService;
    }

    @PreAuthorize("hasAuthority('get:chat_message')")
    public Publisher<MessageNotificationResponse> getChatMessageNotificationData(DataFetchingEnvironment env) {
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

        return chatNotificationPublisher.getChatMessageNotificationPublisherFor(user.getUserId());
    }
}
