package com.example.HardMoneyLending.dto.chat.stats;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatedChatHistoryResponse {
    List<ChatHistoryResponse> chatHistory;

    private int pageNo;
    private int pageSize;
    private int noOfPages;
    private long totalRows;
}
