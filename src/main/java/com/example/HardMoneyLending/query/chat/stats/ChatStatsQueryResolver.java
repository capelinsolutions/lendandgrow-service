package com.example.HardMoneyLending.query.chat.stats;

import com.example.HardMoneyLending.dto.chat.stats.PaginatedChatHistoryResponse;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.services.chat.stats.IChatStatsService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.Message;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Component
@Validated
@PreAuthorize("isAuthenticated()")
public class ChatStatsQueryResolver implements GraphQLQueryResolver {

    private final IChatStatsService chatStatsService;
    private final IUserService userService;

    public ChatStatsQueryResolver(final IChatStatsService chatStatsService,
                                  final IUserService userService) {
        this.chatStatsService = chatStatsService;
        this.userService = userService;
    }


    public Message<PaginatedChatHistoryResponse> getAllChatHistory( int pageNo, int pageSize) {
        System.out.println(pageNo+" DATA: "+pageSize);

        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        return chatStatsService.getAllChatHistory(user.getUserId(), 5, 0);
    }

    public Message<Long> getTotalNoOfContactedInvestor() {
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        return chatStatsService.getTotalNoOfContactedInvestor(user.getUserId());
    }

    public Message<Long> getTotalNoOfContactedBorrower() {
        User user = userService.loadByUsername(getContext().getAuthentication().getName());
        return chatStatsService.getTotalNoOfContactedBorrower(user.getUserId());
    }
}
