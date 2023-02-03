package com.example.HardMoneyLending.models.chat.stats;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_stats")
public class ChatStats {
    @Id
    private String id;
    private String recipientName;
    private String recipientPicture;
    private String senderId;
    private String senderUserType;
    private String recipientId;
    private String recipientUserType;
    private String lastMessageContent;
    private LocalDateTime created;
}
