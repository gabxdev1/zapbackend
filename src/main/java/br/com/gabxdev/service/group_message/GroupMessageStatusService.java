package br.com.gabxdev.service.group_message;

import br.com.gabxdev.Rules.GroupMembershipRules;
import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.messaging.producer.Producer;
import br.com.gabxdev.model.GroupMessage;
import br.com.gabxdev.model.GroupMessageStatus;
import br.com.gabxdev.model.enums.MessageStatus;
import br.com.gabxdev.model.pk.GroupMessageStatusId;
import br.com.gabxdev.notification.dto.GroupMessageStatusSendNotifier;
import br.com.gabxdev.repository.GroupMessageStatusRepository;
import br.com.gabxdev.service.group.GroupMemberService;
import br.com.gabxdev.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.gabxdev.config.RabbitMQConfig.Exchanges.DIRECT_CHAT_EVENTS;
import static br.com.gabxdev.config.RabbitMQConfig.RoutingKeys.GROUP_MESSAGE_STATUS;

@Service
@RequiredArgsConstructor
public class GroupMessageStatusService {

    private final GroupMessageStatusRepository repository;

    private final GroupMessageService groupMessageService;

    private final GroupMembershipRules groupMembershipRules;

    private final GroupMemberService groupMemberService;

    private final UserService userService;

    private final AuthUtil authUtil;

    private final Producer producer;

    private boolean existsById(GroupMessageStatusId id) {
        return repository.existsById(id);
    }

    public List<GroupMessageStatus> findAllGroupMessageStatusForUser() {
        var currentUserId = authUtil.getCurrentUser().getId();

        return this.repository.findAllGroupMessageStatusForUser(currentUserId);
    }

    @Transactional
    public Optional<GroupMessageStatus> markMessageAsRead(UUID messageId) {
        var currentUser = authUtil.getCurrentUser();
        var groupMessage = groupMessageService.findByIdOrThrowNotFound(messageId);

        groupMembershipRules.assertUserIsMemberOfGroupThrowForbidden(groupMessage.getGroup().getId(), currentUser.getId());

        this.assertCurrentUserNotSender(groupMessage, currentUser.getId());

        repository.markAsRead(currentUser.getId(), messageId);

        if (allMembersRead(groupMessage)) {
            var newStatusNoti = GroupMessageStatusSendNotifier
                    .builder()
                    .groupMessageId(groupMessage.getId())
                    .messageStatus(MessageStatus.READ)
                    .build();

            producer.sendMessage(newStatusNoti, DIRECT_CHAT_EVENTS, GROUP_MESSAGE_STATUS);
        }

        var groupMessageStatusId = GroupMessageStatusId.builder()
                .groupMessageId(groupMessage.getId())
                .userId(currentUser.getId())
                .build();

        return repository.findById(groupMessageStatusId);
    }

    @Transactional
    public Optional<GroupMessageStatus> markMessageAsReceived(UUID messageId) {
        var currentUser = authUtil.getCurrentUser();
        var groupMessage = groupMessageService.findByIdOrThrowNotFound(messageId);

        var messageReceivedId = GroupMessageStatusId.builder()
                .groupMessageId(groupMessage.getId())
                .userId(currentUser.getId())
                .build();

        if (existsById(messageReceivedId)) {
            return Optional.empty();
        }

        groupMembershipRules.assertUserIsMemberOfGroupThrowForbidden(groupMessage.getGroup().getId(), currentUser.getId());

        this.assertCurrentUserNotSender(groupMessage, currentUser.getId());

        var user = this.userService.findByIdOrThrowNotFound(currentUser.getId());

        var messageStatus = GroupMessageStatus.builder()
                .id(messageReceivedId)
                .message(groupMessage)
                .user(user)
                .receivedAt(Instant.now())
                .status(MessageStatus.RECEIVED)
                .build();


        if (isNewStatus(groupMessage)) {
            var newStatusNoti = GroupMessageStatusSendNotifier
                    .builder()
                    .groupMessageId(groupMessage.getId())
                    .messageStatus(MessageStatus.RECEIVED)
                    .build();

            producer.sendMessage(newStatusNoti, DIRECT_CHAT_EVENTS, GROUP_MESSAGE_STATUS);
        }

        var response = repository.save(messageStatus);

        return Optional.of(response);
    }


    private void assertCurrentUserNotSender(GroupMessage groupMessage, Long userId) {
        if (groupMessage.getSender().getId().equals(userId)) {
            throw new ForbiddenException("You cannot mark message as read if you are sender.");
        }
    }

    private boolean isNewStatus(GroupMessage groupMessage) {
        return repository.countFirstByMessage(groupMessage) == 0;
    }

    private boolean allMembersRead(GroupMessage groupMessage) {
        return repository.countReadByMessageAndStatus(groupMessage, MessageStatus.READ) ==
               (groupMemberService.countMembersByGroup(groupMessage.getGroup()) - 1);
    }
}
