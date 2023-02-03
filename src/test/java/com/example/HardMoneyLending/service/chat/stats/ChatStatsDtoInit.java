package com.example.HardMoneyLending.service.chat.stats;

import com.example.HardMoneyLending.dto.chat.stats.ChatStatsRequest;

public class ChatStatsDtoInit {

    public static ChatStatsRequest createChatStatsData1() {
        return ChatStatsRequest.builder()
                .lastMessageContent("Content 1")
                //62ce957846f95307878cf8e1
                .senderId("62d1089fd333ae180f94c058")
                .senderUserType("investor")
                .recipientId("62ce957846f95307878cf8e1")
                .recipientUserType("borrower")
                .build();
    }

    public static ChatStatsRequest createChatStatsData2() {
        return ChatStatsRequest.builder()
                .lastMessageContent("Content 2")
                .senderId("62d1089fd333ae180f94c058")
                .senderUserType("investor")
                .recipientId("62d7cf4b07a224003a0d4dc0")
                .recipientUserType("borrower")
                .build();
    }

    public static ChatStatsRequest createChatStatsData3() {
        return ChatStatsRequest.builder()
                .lastMessageContent("Content 3")
                .senderId("62d1089fd333ae180f94c058")
                .senderUserType("investor")
                .recipientId("62df97f9e9327512d25020de")
                .recipientUserType("borrower")
                .build();
    }

}
