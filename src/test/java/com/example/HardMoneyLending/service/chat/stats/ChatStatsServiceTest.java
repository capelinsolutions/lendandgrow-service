package com.example.HardMoneyLending.service.chat.stats;

import com.example.HardMoneyLending.HardMoneyLendingApplication;
import com.example.HardMoneyLending.dto.chat.stats.ChatHistoryResponse;
import com.example.HardMoneyLending.dto.chat.stats.PaginatedChatHistoryResponse;
import com.example.HardMoneyLending.services.chat.stats.IChatStatsService;
import com.example.HardMoneyLending.utils.ListMessage;
import com.example.HardMoneyLending.utils.Message;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = HardMoneyLendingApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ChatStatsServiceTest {

    @Autowired
    private IChatStatsService service;

    @Before
    public void init() {
        service.create(ChatStatsDtoInit.createChatStatsData1());
        service.create(ChatStatsDtoInit.createChatStatsData2());
        service.create(ChatStatsDtoInit.createChatStatsData3());
    }

    @Test
    public void getAllChatHistory_withValidSenderId() {
        /*Message<PaginatedChatHistoryResponse> response = service.getAllChatHistory("1", 5, 0);

        System.out.println("Last message at: "+response.getData().getChatHistory().get(0).getLastMessageAt());
        System.out.println("Last message at: "+response.getData().getChatHistory().get(1).getLastMessageAt());
        System.out.println("Last message at: "+response.getData().getChatHistory().get(2).getLastMessageAt());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getData().getChatHistory().size()).isEqualTo(3);*/
    }

    @Test
    public void getTotalNoOfContactedInvestor() {
        /*Message<Long> response = service.getTotalNoOfContactedInvestor("1");

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getData()).isEqualTo(2);*/
    }

    /*@After
    public void destroy() {
        service.deleteBySenderId("1");
    }*/
}
