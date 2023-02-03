package com.example.HardMoneyLending.repositories.chat.stats;

import com.example.HardMoneyLending.models.chat.stats.ChatStats;
import com.example.HardMoneyLending.utils.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatStatsRepository extends MongoRepository<ChatStats, String> {
    Page<ChatStats> findBySenderId(String senderId, Pageable pageable);

    List<ChatStats> findBySenderId(String senderId);

    void deleteBySenderId(String id);

    ChatStats findBySenderIdAndRecipientId(String senderId, String recipientId);
}
