package com.example.HardMoneyLending.models.chat.message;


import com.example.HardMoneyLending.dto.chat.notification.MessageNotificationResponse;
import com.example.HardMoneyLending.models.chat.message.ChatMessage;
import com.example.HardMoneyLending.repositories.chat.ChatMessageRepository;
import com.example.HardMoneyLending.services.chat.IChatMessageService;
import com.example.HardMoneyLending.services.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

@Slf4j
@Component
public class ChatNotificationPublisher {

    private final FluxProcessor<MessageNotificationResponse, MessageNotificationResponse> processor;
    private final FluxSink sink;
    private final IUserService userService;
    private final ChatMessageRepository chatMessageRepository;
    private final IChatMessageService chatMessageService;

    public ChatNotificationPublisher(IUserService userService, ChatMessageRepository chatMessageRepository, IChatMessageService chatMessageService) {
        this.processor = DirectProcessor.create();
        this.sink = processor.sink();
        this.userService = userService;
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageService = chatMessageService;
    }

    public void publishNotification(MessageNotificationResponse messageNotificationResponse) {
        log.info("METHOD: (publishNotification) Publishing message notification.");
        sink.next(messageNotificationResponse);
    }

    public Publisher<MessageNotificationResponse> getChatMessageNotificationPublisherFor(String id) {
        return processor
                .filter(e -> e.getUserId().equals(id))
                .map(messageNotification -> {
            log.info("METHOD: (getChatMessageNotificationPublisherFor) with user id: "+id);
            return messageNotification;
        });
    }
}
