package br.com.gabxdev.service.group_message;

import br.com.gabxdev.Rules.GroupMembershipRules;
import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.dto.request.group_message.GroupMessageSendRequest;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.GroupMessage;
import br.com.gabxdev.model.enums.MessageStatus;
import br.com.gabxdev.repository.GroupMessageRepository;
import br.com.gabxdev.service.group.GroupService;
import br.com.gabxdev.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupMessageService {

    private final GroupMessageRepository repository;

    private final AuthUtil authUtil;

    private final GroupMembershipRules groupMembershipRules;

    private final GroupService groupService;

    private final UserService userService;

    public GroupMessage findByIdOrThrowNotFound(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Group message %s not found".formatted(id.toString())));
    }

    public GroupMessage save(GroupMessageSendRequest messageSendRequest) {
        var group = groupService.findByIdOrThrowNotFound(messageSendRequest.groupId());

        var currentUserId = authUtil.getCurrentUser().getId();

        groupMembershipRules.assertUserIsMemberOfGroupThrowForbidden(group.getId(), currentUserId);

        var user = userService.findByIdOrThrowNotFound(currentUserId);

        var groupMessage = GroupMessage.builder()
                .id(messageSendRequest.messageId())
                .group(group).sender(user)
                .content(messageSendRequest.content())
                .status(MessageStatus.SENT)
                .build();

        return repository.save(groupMessage);
    }

    public GroupMessage updateGroupMessageStatus(UUID groupMessageId, MessageStatus messageStatus) {
        var groupMessage = findByIdOrThrowNotFound(groupMessageId);

        groupMessage.setStatus(messageStatus);

        return repository.save(groupMessage);
    }

    public List<GroupMessage> findAllGroupMessages() {
        var currentUserId = authUtil.getCurrentUser().getId();

        return repository.findAllGroupMessageForUser(currentUserId);
    }
}
