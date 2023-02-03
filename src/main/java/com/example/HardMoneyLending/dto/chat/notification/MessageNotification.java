package com.example.HardMoneyLending.dto.chat.notification;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "abc")
public class MessageNotification{
    @Id
    private String senderName;
    private String senderId;
    private String content;
    private int count;
    private Date sendAt;
    private String profileUrl;
}
