package com.example.HardMoneyLending.service.chat.chat_message;

import com.example.HardMoneyLending.HardMoneyLendingApplication;
import com.example.HardMoneyLending.dto.chat.notification.MessageNotification;
import com.example.HardMoneyLending.dto.chat.notification.MessageNotificationResponse;
import com.example.HardMoneyLending.repositories.chat.ChatMessageRepository;
import com.example.HardMoneyLending.services.chat.IChatMessageService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = HardMoneyLendingApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ChatMessageServiceTest {

    @Autowired
    private IChatMessageService service;

    @Autowired
    private ChatMessageRepository repo;

    @Before
    public void init() {
        repo.save(ChatMessageDtoInit.createOne_withUnreadStatus());
        repo.save(ChatMessageDtoInit.createSecond_withUnreadStatus());
        repo.save(ChatMessageDtoInit.createThird_withUnreadStatus());
        repo.save(ChatMessageDtoInit.createOne_withReadStatus());
        repo.save(ChatMessageDtoInit.createSecond_withReadStatus());
    }

    @Test
    public void getAllUnreadMessages_withUserId() {
        Message<MessageNotificationResponse> messages = service.getAllUnreadMessages("630379f8e114a041a2b0357d");

        assertThat(messages).isNull();
    }

    @After
    public void destroy() {
        repo.deleteByRecipientId("630379f8e114a041a2b0357d");
    }
}
