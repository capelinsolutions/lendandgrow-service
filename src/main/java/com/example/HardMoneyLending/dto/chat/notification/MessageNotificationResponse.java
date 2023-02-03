package com.example.HardMoneyLending.dto.chat.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageNotificationResponse {
    private String userId;
    private List<MessageNotification> notifications;
    private int totalCount;
}
