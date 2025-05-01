package br.com.gabxdev.messaging.consumer.group;

import br.com.gabxdev.mapper.GroupMapper;
import br.com.gabxdev.messaging.wrapper.MessageWrapper;
import br.com.gabxdev.notification.dto.GroupCreatedNotification;
import br.com.gabxdev.notification.notifier.GroupNotifier;
import br.com.gabxdev.service.group.GroupService;
import br.com.gabxdev.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.gabxdev.config.RabbitMQConfig.QueueNames.GROUP_CREATED_NOTIFICATION;

@Component
@RequiredArgsConstructor
public class GroupConsumer {

    private final UserService userService;

    private final GroupService groupService;

    private final GroupMapper groupMapper;

    private final GroupNotifier notifier;

    @RabbitListener(queues = GROUP_CREATED_NOTIFICATION)
    public void processGroupCreatedNotification(MessageWrapper<GroupCreatedNotification> request) {
        var groupCreatedNotification = request.request();

        var allEmailsById = userService.findAllEmailsByIdIn(groupCreatedNotification.membersId());

        var group = groupService.findGroupFullDetailsByIdOrThrow(groupCreatedNotification.groupId());

        var groupGetResponse = groupMapper.toGroupGetResponse(group);

        notifier.notifyGroupCreated(allEmailsById, groupGetResponse);
    }
}
