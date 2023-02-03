package com.example.HardMoneyLending.repositories.chat;

import com.example.HardMoneyLending.models.chat.message.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    Page<ChatMessage> findAllBySenderIdAndRecipientIdOrSenderIdAndRecipientId(String senderId, String recipientId, String senderId1, String recipientId1, Pageable pageable);

    void deleteByRecipientId(String s);

    List<ChatMessage> findAllByRecipientIdAndIsReadOrderBySendAtDesc(String userId, boolean b);

    List<ChatMessage> findAllByRecipientIdAndSenderIdAndIsRead(String userId, String senderId, boolean b);
}
