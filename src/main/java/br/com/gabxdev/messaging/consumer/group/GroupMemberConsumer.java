package br.com.gabxdev.messaging.consumer.group;

import br.com.gabxdev.mapper.GroupMapper;
import br.com.gabxdev.mapper.GroupMessageMapper;
import br.com.gabxdev.messaging.wrapper.MessageWrapper;
import br.com.gabxdev.notification.dto.GroupDataNotifierToNewMember;
import br.com.gabxdev.notification.dto.GroupNewMemberNotification;
import br.com.gabxdev.notification.notifier.GroupNotifier;
import br.com.gabxdev.repository.GroupMessageRepository;
import br.com.gabxdev.repository.GroupMessageStatusRepository;
import br.com.gabxdev.repository.UserRepository;
import br.com.gabxdev.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static br.com.gabxdev.config.RabbitMQConfig.QueueNames.GROUP_NEW_MEMBER_NOTIFICATION;

@Component
@RequiredArgsConstructor
public class GroupMemberConsumer {

    private final GroupService groupService;

    private final GroupMapper groupMapper;

    private final GroupNotifier notifier;

    private final GroupMessageRepository groupMessageRepository;

    private final GroupMessageMapper groupMessageMapper;

    private final UserRepository userRepository;

    @RabbitListener(queues = GROUP_NEW_MEMBER_NOTIFICATION)
    public void processGroupCreatedNotification(MessageWrapper<GroupNewMemberNotification> request) {
        var groupNewMemberNotification = request.request();

        var group = groupService.findGroupFullDetailsByIdOrThrow(groupNewMemberNotification.groupId());

        var groupDataNotifierToNewMember = new ArrayList<GroupDataNotifierToNewMember>();

        groupNewMemberNotification.newMembersIds().forEach(id -> {
            userRepository.findEmailByUserId(id).ifPresent(email -> {
                var allGroupMessageByGroupIdForUser = groupMessageRepository.findAllGroupMessageByGroupIdForUser(id, group.getId());

                var response = GroupDataNotifierToNewMember
                        .builder()
                        .userEmail(email)
                        .groupMessageResponses(groupMessageMapper.toGroupMessageResponse(allGroupMessageByGroupIdForUser))
                        .build();

                groupDataNotifierToNewMember.add(response);
            });
        });

        var groupGetResponse = groupMapper.toGroupGetResponse(group);

        var emails = group.getMembers().stream()
                .map(groupMember -> groupMember.getUser().getEmail()).toList();

        notifier.notifyNewMember(emails, groupGetResponse, groupDataNotifierToNewMember);
    }
}
