package com.example.HardMoneyLending.services.chat.stats;


import com.example.HardMoneyLending.dto.chat.stats.ChatStatsRequest;
import com.example.HardMoneyLending.dto.chat.stats.PaginatedChatHistoryResponse;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.chat.stats.ChatStats;
import com.example.HardMoneyLending.utils.Message;

public interface IChatStatsService {
    Message<ChatStats> create(ChatStatsRequest dto);

    Message<PaginatedChatHistoryResponse> getAllChatHistory(String senderId, int pageSize, int pageNo) throws BadRequestException, EntityNotFoundException;

    Message<Long> getTotalNoOfContactedInvestor(String senderId) throws BadRequestException, EntityNotFoundException;

    Message<Long> getTotalNoOfContactedBorrower(String senderId) throws BadRequestException, EntityNotFoundException;

    void deleteBySenderId(String id);

    void save(ChatStats chatStats);

    ChatStats getBySenderIdAndRecipientId(String userId, String userId1);
}
