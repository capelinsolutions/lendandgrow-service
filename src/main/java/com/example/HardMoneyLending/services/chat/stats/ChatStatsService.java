package com.example.HardMoneyLending.services.chat.stats;

import com.example.HardMoneyLending.dto.chat.stats.ChatHistoryResponse;
import com.example.HardMoneyLending.dto.chat.stats.ChatStatsRequest;
import com.example.HardMoneyLending.dto.chat.stats.PaginatedChatHistoryResponse;
import com.example.HardMoneyLending.exception.general_exception_graphql.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotSavedException;
import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.models.chat.stats.ChatStats;
import com.example.HardMoneyLending.models.investor.Investor;
import com.example.HardMoneyLending.repositories.chat.stats.ChatStatsRepository;
import com.example.HardMoneyLending.services.borrower.IBorrowerService;
import com.example.HardMoneyLending.services.investor.IInvestorService;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatStatsService implements IChatStatsService{

    private final ChatStatsRepository repo;
    private final IBorrowerService borrowerService;
    private final IInvestorService investorService;


    public ChatStatsService(ChatStatsRepository repo, IInvestorService investorService, IBorrowerService borrowerService) {
        this.repo = repo;
        this.borrowerService = borrowerService;
        this.investorService = investorService;
    }

    @Override
    public Message<ChatStats> create(ChatStatsRequest dto) throws EntityNotSavedException {
        log.info("Creating chat stats with provided dto.");
        String name = null;
        String picture = null;

        try {
            Investor investor = investorService.getInvestorByUserId(dto.getRecipientId());
            name = investor.getName();
            picture = investor.getProfileImageURL();
        } catch (EntityNotFoundException | BadRequestException exception) {
            log.error("Recipient is not an investor.");
            Borrower borrower = borrowerService.getBorrowerByUserId(dto.getRecipientId());
            if (borrower == null) {
                log.error("Recipient is not a borrower");
                log.error("Recipient id is not valid.");
                return new Message<ChatStats>()
                        .setMessage("Recipient id is not valid.")
                        .setStatus(HttpStatus.BAD_REQUEST.value())
                        .setCode(HttpStatus.BAD_REQUEST.toString());
            }
            name = borrower.getName();
            picture = borrower.getProfileImageURL();
        }

        ChatStats chatStats = new ChatStats();
        chatStats.setRecipientName(name);
        chatStats.setRecipientPicture(picture);
        chatStats.setCreated(LocalDateTime.now());
        chatStats.setRecipientId(dto.getRecipientId());
        chatStats.setRecipientUserType(dto.getRecipientUserType());
        chatStats.setSenderId(dto.getSenderId());
        chatStats.setSenderUserType(dto.getSenderUserType());
        chatStats.setLastMessageContent(dto.getLastMessageContent());

        try {
            chatStats = repo.save(chatStats);
            log.info("Chat history is saved successfully.");
            return new Message<ChatStats>()
                    .setMessage("Chat history is saved successfully.")
                    .setCode(HttpStatus.OK.toString())
                    .setStatus(HttpStatus.OK.value())
                    .setData(chatStats);
        } catch (Exception e) {
            log.error("Error: "+e.getLocalizedMessage());
            return new Message<ChatStats>()
                    .setMessage("Chat history can not be saved.")
                    .setCode(HttpStatus.BAD_REQUEST.toString())
                    .setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    /**
     * Create chat stats on every message for getting the recent messages by pagination module
     */
    @Override
    public Message<PaginatedChatHistoryResponse> getAllChatHistory(String senderId, int pageSize, int pageNo)
            throws BadRequestException, EntityNotFoundException{
        log.info("Getting all chat history for user id: "+senderId);
        if (senderId == null || senderId.isEmpty()) {
            log.error("Sender id should not be empty or null.");
            throw new BadRequestException("Sender id should not be empty or null.");
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("created").descending());
        Page<ChatStats> chatStats = repo.findBySenderId(senderId, pageable);
        if (chatStats == null || chatStats.isEmpty()) {
            log.error("No chat history found for user id: "+senderId);
            throw new EntityNotFoundException("No chat history found for user id: "+senderId);
        }

        List<ChatHistoryResponse> mappedData = chatStats.get()
                .map(e -> {
                   ChatHistoryResponse history = new ChatHistoryResponse();
                   history.setName(e.getRecipientName());
                   history.setPicture(e.getRecipientPicture());
                   history.setContent(e.getLastMessageContent());
                   history.setSenderId(e.getSenderId());
                   history.setRecipientId(e.getRecipientId());
                   history.setLastMessageAt(getTimePresentation(e.getCreated()));
                   return history;
                }).collect(Collectors.toList());

        PaginatedChatHistoryResponse response = new PaginatedChatHistoryResponse();
        response.setChatHistory(mappedData);
        response.setPageNo(pageNo);
        response.setPageSize(pageSize);
        response.setTotalRows(chatStats.getTotalElements());
        response.setNoOfPages(chatStats.getTotalPages());


        log.info("Successfully fetched all chat history for user id: "+senderId);
        return new Message<PaginatedChatHistoryResponse>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Successfully fetched all chat history.")
                .setData(response);
    }

    private String getTimePresentation(LocalDateTime created) {
        long second = created.until(LocalDateTime.now(), ChronoUnit.SECONDS);
        long min = created.until(LocalDateTime.now(), ChronoUnit.MINUTES);
        long hour = created.until(LocalDateTime.now(), ChronoUnit.HOURS);
        long day = created.until(LocalDateTime.now(), ChronoUnit.DAYS);
        long week = created.until(LocalDateTime.now(), ChronoUnit.WEEKS);
        long month = created.until(LocalDateTime.now(), ChronoUnit.MONTHS);

        if(month > 0)
            return month + ( month > 1 ? " months ago" : " month ago");
        if(month <= 0 && week > 0)
            return week + ( week > 1 ? " weeks ago" : " week ago");
        if(month <= 0 && week <= 0 && day > 0)
            return day + ( day > 1 ? " days ago" : " day ago");
        if(month <= 0 && week <= 0 && day <= 0 && hour > 0)
            return hour + ( hour > 1 ? " hours ago" : " hour ago");
        if(month <= 0 && week <= 0 && day <= 0 && hour <= 0 && min > 0)
            return min + ( min > 1 ? " minutes ago" : " minute ago");
        if(month <= 0 && week <= 0 &&  day <= 0 && hour <= 0 && min <= 0 && second > 0)
            return second + ( second > 1 ? " seconds ago" : " second ago");
        return "Years Ago";
    }

    /**
     * Get total no of investors contacted by the user
     */
    @Override
    public Message<Long> getTotalNoOfContactedInvestor(String senderId) throws BadRequestException, EntityNotFoundException {
        log.info("Getting the total no of contacted investors with user id: "+senderId);
        if (senderId == null || senderId.isEmpty()) {
            log.error("Sender id should not be empty or null.");
            throw new BadRequestException("Sender id should not be empty or null.");
        }

        List<ChatStats> recipientIdList = repo.findBySenderId(senderId);
        if (recipientIdList == null || recipientIdList.isEmpty()) {
            log.error("No chat history found for user id: "+senderId);
            return new Message<Long>()
                    .setMessage("No investor contacted.")
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString())
                    .setData(0L);
        }

        long count = investorService.countNoOfInvestorByUserIdList(
                recipientIdList.stream()
                        .map(e -> e.getRecipientId())
                        .collect(Collectors.toList())
        );
        return new Message<Long>()
                .setMessage("Successfully fetched the data.")
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setData(count);
    }

    /**
     * Get total no of borrower contacted by the user
     */
    @Override
    public Message<Long> getTotalNoOfContactedBorrower(String senderId) throws BadRequestException, EntityNotFoundException {
        log.info("Getting the total no of contacted borrower with user id: "+senderId);
        if (senderId == null || senderId.isEmpty()) {
            log.error("Sender id should not be empty or null.");
            throw new BadRequestException("Sender id should not be empty or null.");
        }

        List<ChatStats> recipientIdList = repo.findBySenderId(senderId);
        if (recipientIdList == null || recipientIdList.isEmpty()) {
            log.error("No chat history found for user id: "+senderId);
            return new Message<Long>()
                    .setMessage("Successfully fetched the data.")
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString())
                    .setData(0L);
        }

        long count = borrowerService.countNoOfBorrowersByUserIdList(
                recipientIdList.stream()
                        .map(e -> e.getRecipientId())
                        .collect(Collectors.toList())
        );
        return new Message<Long>()
                .setMessage("Successfully fetched the data.")
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setData(count);
    }

    @Override
    public void deleteBySenderId(String id) {
        repo.deleteBySenderId(id);
    }

    @Override
    public void save(ChatStats chatStats) {
        repo.save(chatStats);
    }

    @Override
    public ChatStats getBySenderIdAndRecipientId(String senderId, String recipientId) {
        ChatStats chatStats = repo.findBySenderIdAndRecipientId(senderId, recipientId);
        return chatStats;
    }
}
