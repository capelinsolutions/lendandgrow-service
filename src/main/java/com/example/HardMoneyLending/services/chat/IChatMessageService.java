package com.example.HardMoneyLending.services.chat;

import com.example.HardMoneyLending.dto.chat.ChatMessageRequest;
import com.example.HardMoneyLending.dto.chat.PaginatedChatRequest;
import com.example.HardMoneyLending.dto.chat.PaginatedChatResponse;
import com.example.HardMoneyLending.dto.chat.notification.MessageNotification;
import com.example.HardMoneyLending.dto.chat.notification.MessageNotificationResponse;
import com.example.HardMoneyLending.exception.general_exception.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.chat.message.ChatMessage;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import org.springframework.data.domain.Pageable;

public interface IChatMessageService {

    Message<ChatMessage> createChatMessage(ChatMessageRequest data, User user) throws BadRequestException;

    // Get message by last previous messages by recipient id and user id with pagination
    Message<PaginatedChatResponse> getMessagesByRecipientIdAndUserIdByPagination(PaginatedChatRequest chatRequest, Pageable pageable) throws EntityNotFoundException;

    void markAllUnreadMessageToRead(String userId, String senderId);

    Message<MessageNotificationResponse> getAllUnreadMessages(String userId)  throws EntityNotFoundException;
}
