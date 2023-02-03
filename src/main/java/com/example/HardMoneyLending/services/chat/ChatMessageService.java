package com.example.HardMoneyLending.services.chat;

import com.example.HardMoneyLending.dto.chat.ChatMessageRequest;
import com.example.HardMoneyLending.dto.chat.PaginatedChatRequest;
import com.example.HardMoneyLending.dto.chat.PaginatedChatResponse;
import com.example.HardMoneyLending.dto.chat.notification.MessageNotification;
import com.example.HardMoneyLending.dto.chat.notification.MessageNotificationResponse;
import com.example.HardMoneyLending.exception.general_exception.BadRequestException;
import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import com.example.HardMoneyLending.models.borrower.Borrower;
import com.example.HardMoneyLending.models.chat.message.ChatMessage;
import com.example.HardMoneyLending.models.chat.message.ChatMessagePublisher;
import com.example.HardMoneyLending.models.chat.message.ChatNotificationPublisher;
import com.example.HardMoneyLending.models.chat.stats.ChatStats;
import com.example.HardMoneyLending.models.investor.Investor;
import com.example.HardMoneyLending.models.user.User;
import com.example.HardMoneyLending.models.user.UserType;
import com.example.HardMoneyLending.repositories.chat.ChatMessageRepository;
import com.example.HardMoneyLending.repositories.user.user_type.UserTypeRepository;
import com.example.HardMoneyLending.services.borrower.IBorrowerService;
import com.example.HardMoneyLending.services.chat.stats.IChatStatsService;
import com.example.HardMoneyLending.services.investor.IInvestorService;
import com.example.HardMoneyLending.services.user.IUserService;
import com.example.HardMoneyLending.utils.Enums;
import com.example.HardMoneyLending.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatMessageService implements IChatMessageService{

    private final ChatMessageRepository repo;
    private final IUserService iUserService;
    private final UserTypeRepository userTypeRepo;
    private final IBorrowerService borrowerService;
    private final IInvestorService investorService;
    private final IChatStatsService chatStatsService;
    private final ChatMessagePublisher publisher;
    private final ChatNotificationPublisher notificationPublisher;

    public ChatMessageService(final ChatMessageRepository repo,
                              IUserService iUserService,
                              UserTypeRepository userTypeRepo,
                              IBorrowerService borrowerService,
                              IInvestorService investorService,
                              IChatStatsService chatStatsService,
                              ChatMessagePublisher publisher,
                              @Lazy ChatNotificationPublisher notificationPublisher) {
        this.repo = repo;
        this.iUserService = iUserService;
        this.userTypeRepo = userTypeRepo;
        this.borrowerService = borrowerService;
        this.investorService = investorService;
        this.chatStatsService = chatStatsService;
        this.publisher = publisher;
        this.notificationPublisher = notificationPublisher;
    }

    @Override
    public Message<ChatMessage> createChatMessage(ChatMessageRequest data, User user) throws BadRequestException {

        ChatMessage chatMessage = new ChatMessage(
                null,
                data.getContent(),
                user.getUserId(),
                data.getRecipientId(),
                new Date(),
                false
        );

        try {
            ChatMessage chatMessage1 = repo.save(chatMessage);

            // Process chat stats
            createChatStatsWithSendMessage(data, user, chatMessage);

            log.info("Chat message is saved in our system.");
            publisher.publish(chatMessage);
            Message<MessageNotificationResponse> allUnreadMessages = null;
            try {
                allUnreadMessages = getAllUnreadMessages(data.getRecipientId());
            } catch (EntityNotFoundException e) {
                log.error("Unread messages not found.");
            }

            if(allUnreadMessages != null && allUnreadMessages.getStatus() == HttpStatus.OK.value())
                notificationPublisher.publishNotification(allUnreadMessages.getData());

            return new Message<ChatMessage>()
                    .setData(chatMessage)
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString())
                    .setMessage("Published message for user: "+data.getRecipientId());
        } catch (Exception e) {
            log.error("ERROR: "+e.getLocalizedMessage());
            throw new BadRequestException("Chat message is not saved.");
        }
    }

    private void createChatStatsWithSendMessage(ChatMessageRequest data, User user, ChatMessage chatMessage) {
        User sender = iUserService.getUserByUserId(user.getUserId());
        User recipient = iUserService.getUserByUserId(data.getRecipientId());

        // Check whether sender and recipient is already exists
        ChatStats savedChatStats = chatStatsService.getBySenderIdAndRecipientId(sender.getUserId(), recipient.getUserId());
        if (savedChatStats != null) {
            savedChatStats.setLastMessageContent(chatMessage.getContent());
            savedChatStats.setCreated(LocalDateTime.now());
            chatStatsService.save(savedChatStats);
            return;
        }

        ChatStats chatStats = new ChatStats();

        chatStats.setSenderUserType(sender.getUserType());
        chatStats.setSenderId(sender.getUserId());
        UserType recipientUserType = userTypeRepo.findById(recipient.getUserType()).get();

        Borrower borrower = null;
        Investor investor = null;
        if(recipientUserType.getRole().equalsIgnoreCase(Enums.UserType.BORROWER.name())){
            borrower = borrowerService.getBorrowerByUserId(recipient.getUserId());
        }else{
            investor = investorService.getInvestorByUserId(recipient.getUserId());
        }

        if (borrower == null && investor == null) {
            log.error("Borrower and Investor both should not be null;");
            return;
        }

        chatStats.setRecipientPicture((investor == null ? borrower.getProfileImageURL(): investor.getProfileImageURL()));
        chatStats.setRecipientName(recipient.getName());
        chatStats.setRecipientUserType(recipient.getUserType());
        chatStats.setRecipientId(recipient.getUserId());

        chatStats.setCreated(LocalDateTime.now());
        chatStats.setLastMessageContent(chatMessage.getContent());

        chatStatsService.save(chatStats);
    }

    // Get message by last previous messages by recipient id and user id with pagination
    @Override
    public Message<PaginatedChatResponse> getMessagesByRecipientIdAndUserIdByPagination(PaginatedChatRequest chatRequest, Pageable pageable)
            throws EntityNotFoundException{
        log.info("Getting the messages by given recipient and user id with pagination...");
        Page<ChatMessage> resulting = null;

        if(chatRequest.getRecipientId()==null || chatRequest.getRecipientId().isEmpty()){
            log.error("Provided Recipient Id should not be null or empty.");
            throw new EntityNotFoundException("Provided Recipient Id should not be null or empty.");
        }
        else if (chatRequest.getSenderId()==null || chatRequest.getSenderId().isEmpty()){
            log.error("Provided Sender Id should not be null or empty.");
            throw new EntityNotFoundException("Provided Sender Id should not be null or empty.");
        }
        else {
            resulting = repo.findAllBySenderIdAndRecipientIdOrSenderIdAndRecipientId(chatRequest.getSenderId(), chatRequest.getRecipientId(), chatRequest.getRecipientId(), chatRequest.getSenderId() , pageable);

            if (resulting.getContent() == null || resulting.getContent().isEmpty())
                throw new EntityNotFoundException("No data found.");

            PaginatedChatResponse response = PaginatedChatResponse.builder()
                    .chatMessages(resulting.getContent())
                    .pageNo(chatRequest.getPageNo())
                    .pageSize(chatRequest.getPageSize())
                    .noOfPages(resulting.getTotalPages())
                    .totalRows(resulting.getTotalElements())
                    .build();


            return new Message<PaginatedChatResponse>()
                    .setStatus(HttpStatus.OK.value())
                    .setCode(HttpStatus.OK.toString())
                    .setMessage("Successfully Fetched.")
                    .setData(response);
        }
    }

    @Override
    public void markAllUnreadMessageToRead(String userId, String senderId) {
        List<ChatMessage> unreadMessages = repo.findAllByRecipientIdAndSenderIdAndIsRead(userId, senderId, false);
        if (unreadMessages == null || unreadMessages.isEmpty()) {
            log.info("No unread messages are available for user id: "+userId);
            return;
        }
        unreadMessages.stream().forEach(e -> {
            e.setRead(true);
            repo.save(e);
        });
    }

    @Override
    public Message<MessageNotificationResponse> getAllUnreadMessages(String userId) throws EntityNotFoundException {
        log.info("Getting all unread message for user id: "+userId);
        List<ChatMessage> unreadMessages = repo.findAllByRecipientIdAndIsReadOrderBySendAtDesc(userId, false);
        if (unreadMessages == null || unreadMessages.isEmpty()) {
            log.info("No unread messages are available for user id: "+userId);
            throw new EntityNotFoundException("No unread messages are available for user id: "+userId);
        }
        List< List<ChatMessage> > groups = unreadMessages.stream().collect( Collectors.collectingAndThen(
                Collectors.groupingBy( p -> p.getSenderId() ),
                m -> new ArrayList<>( m.values())));

        MessageNotificationResponse messageNotification = new MessageNotificationResponse();

        List<MessageNotification> notifications = new ArrayList<>();
        for (List<ChatMessage> msgs : groups) {
            User user = iUserService.getUserByUserId(msgs.get(0).getSenderId());

            MessageNotification m = new MessageNotification();

            UserType userType = iUserService.getUserTypeById(user.getUserType());

            if (userType.getRole().equalsIgnoreCase(Enums.UserType.INVESTOR.toString())) {
                Investor investor = investorService.getInvestorByUserId(user.getUserId());
                m.setContent(msgs.get(0).getContent());
                m.setSendAt(msgs.get(0).getSendAt());
                m.setSenderId(msgs.get(0).getSenderId());
                m.setSenderName(user.getName());
                m.setCount(msgs.size());
                m.setProfileUrl(investor.getProfileImageURL());
                notifications.add(m);
            }else {
                Borrower borrower = borrowerService.getBorrowerByUserId(user.getUserId());
                m.setContent(msgs.get(0).getContent());
                m.setSendAt(msgs.get(0).getSendAt());
                m.setSenderId(msgs.get(0).getSenderId());
                m.setSenderName(user.getName());
                m.setCount(msgs.size());
                m.setProfileUrl(borrower.getProfileImageURL());
                notifications.add(m);
            }
        }
        messageNotification.setUserId(userId);
        messageNotification.setNotifications(notifications);
        messageNotification.setTotalCount(unreadMessages.size());

        log.info("Fetched all unread messages.");
        return new Message<MessageNotificationResponse>()
                .setStatus(HttpStatus.OK.value())
                .setCode(HttpStatus.OK.toString())
                .setMessage("Notifications fetched successfully.")
                .setData(messageNotification);
    }

}
