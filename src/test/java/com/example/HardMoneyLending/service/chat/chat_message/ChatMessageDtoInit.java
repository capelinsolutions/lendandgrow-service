package com.example.HardMoneyLending.service.chat.chat_message;

import com.example.HardMoneyLending.models.chat.message.ChatMessage;

import java.util.Date;

public class ChatMessageDtoInit {

    public static ChatMessage createOne_withUnreadStatus() {
        return ChatMessage.builder().isRead(false)
                .content("first message")
                .sendAt(new Date())
                .senderId("630f333de4b0982085d0f5e0")
                .recipientId("630379f8e114a041a2b0357d")
                .build();
    }

    public static ChatMessage createSecond_withUnreadStatus() {
        return ChatMessage.builder().isRead(false)
                .content("sec message")
                .sendAt(new Date())
                .senderId("630f333de4b0982085d0f5e0")
                .recipientId("630379f8e114a041a2b0357d")
                .build();
    }

    public static ChatMessage createThird_withUnreadStatus() {
        return ChatMessage.builder().isRead(false)
                .content("th message")
                .sendAt(new Date())
                .senderId("6303692de114a041a2b03579")
                .recipientId("630379f8e114a041a2b0357d")
                .build();
    }

    public static ChatMessage createOne_withReadStatus() {
        return ChatMessage.builder().isRead(false)
                .content("first message")
                .sendAt(new Date())
                .senderId("630f72b45ba59771bc790328")
                .recipientId("630379f8e114a041a2b0357d")
                .build();
    }

    public static ChatMessage createSecond_withReadStatus() {
        return ChatMessage.builder().isRead(false)
                .content("first message")
                .sendAt(new Date())
                .senderId("630f72b45ba59771bc790328")
                .recipientId("630379f8e114a041a2b0357d")
                .build();
    }
}
