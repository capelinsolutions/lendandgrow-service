package com.example.HardMoneyLending.models.chat.message;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_message")
public class ChatMessage {

    @Id
    private String id;
    private String content;
    private String senderId; // User id of a sender
    private String recipientId; // User id of a recipient
    private Date sendAt;
    private boolean isRead; // message read by recipient or not
}
