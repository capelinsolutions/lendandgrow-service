package com.example.HardMoneyLending.dto.chat.notification;

import com.example.HardMoneyLending.models.chat.message.ChatMessage;
import com.example.HardMoneyLending.services.chat.ChatMessageService;
import com.example.HardMoneyLending.services.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

@Slf4j
@Component
public class NotificationPublisher {

    /*private final FluxProcessor<MessageNotification, MessageNotification> processor;
    private final FluxSink<MessageNotification> sink;
    private final IUserService userService;
    private final ChatMessageService chatMessageService;

    public NotificationPublisher(IUserService userService, ChatMessageService chatMessageService) {
        this.userService = userService;
        this.chatMessageService = chatMessageService;
        this.processor = DirectProcessor.<~>.create().serialize();
        this.sink = processor.sink();
    }

    public void publish(MessageNotification notificationList) {
        log.info("METHOD: (publish) Publishing message.");
        sink.next(notificationList);
    }

    public Publisher<MessageNotification> getChatMessagePublisher() {
        return processor.map(message -> {
            log.info("METHOD: (getMessagePublisher) Publishing message {}", message);
            return message;
        });
    }*/
}
