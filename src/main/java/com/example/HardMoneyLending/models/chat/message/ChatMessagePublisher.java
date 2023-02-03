package com.example.HardMoneyLending.models.chat.message;

import com.example.HardMoneyLending.dto.chat.notification.MessageNotificationResponse;
import com.example.HardMoneyLending.repositories.chat.ChatMessageRepository;
import com.example.HardMoneyLending.services.chat.IChatMessageService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.emitter.Emitable;
import reactor.core.CorePublisher;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

import java.util.List;


@Slf4j
@Component
public class ChatMessagePublisher {

    private final FluxProcessor<ChatMessage, ChatMessage> processor;
    private final FluxSink<ChatMessage> sink;
    private final IUserService userService;

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessagePublisher(IUserService userService, ChatMessageRepository chatMessageRepository) {
        this.userService = userService;
        this.chatMessageRepository = chatMessageRepository;
        this.processor = DirectProcessor.create();
        this.sink = processor.sink();
    }

    public void publish(ChatMessage chatMessage) {
        log.info("METHOD: (publish) Publishing message.");
        sink.next(chatMessage);
    }

    public Publisher<ChatMessage> getChatMessagePublisher() {
        return processor.map(message -> {
            log.info("METHOD: (getMessagePublisher) Publishing message {}", message);
            return message;
        });
    }

    public Publisher<ChatMessage> getChatMessagePublisherFor(String id, String senderId) {
        return processor
                .filter(chatMessage -> id.equals(chatMessage.getRecipientId()) && senderId.equals(chatMessage.getSenderId()))
                .map(chatMessage -> {
                    log.info("METHOD: (getMessagePublisherFor) Publishing individual "
                            +"subscription for message {}", chatMessage);
                    chatMessage.setRead(true);
                    chatMessage = chatMessageRepository.save(chatMessage);
                    return chatMessage;});
    }
}
