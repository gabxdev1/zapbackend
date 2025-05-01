package br.com.gabxdev.messaging.consumer.group;

import br.com.gabxdev.mapper.GroupMapper;
import br.com.gabxdev.messaging.wrapper.MessageWrapper;
import br.com.gabxdev.notification.dto.GroupNewMemberNotification;
import br.com.gabxdev.notification.notifier.GroupNotifier;
import br.com.gabxdev.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.gabxdev.config.RabbitMQConfig.QueueNames.GROUP_NEW_MEMBER_NOTIFICATION;

@Component
@RequiredArgsConstructor
public class GroupMemberConsumer {

    private final GroupService groupService;

    private final GroupMapper groupMapper;

    private final GroupNotifier notifier;

    @RabbitListener(queues = GROUP_NEW_MEMBER_NOTIFICATION)
    public void processGroupCreatedNotification(MessageWrapper<GroupNewMemberNotification> request) {
        var groupNewMemberNotification = request.request();

        var group = groupService.findGroupFullDetailsByIdOrThrow(groupNewMemberNotification.groupId());

        var groupGetResponse = groupMapper.toGroupGetResponse(group);

        var emails = group.getMembers().stream()
                .map(groupMember -> groupMember.getUser().getEmail()).toList();

        notifier.notifyNewMember(emails, groupGetResponse);
    }
}
