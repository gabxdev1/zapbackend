package br.com.gabxdev.messaging.consumer.group;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.messaging.wrapper.MessageWrapper;
import br.com.gabxdev.notification.dto.GroupMessageStatusSendNotifier;
import br.com.gabxdev.notification.notifier.GroupMessageStatusNotifier;
import br.com.gabxdev.service.group_message.GroupMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.gabxdev.config.RabbitMQConfig.QueueNames.GROUP_MESSAGE_STATUS;

@Component
@RequiredArgsConstructor
public class GroupMessageStatusConsumer {

    private final GroupMessageService groupMessageService;

    private final AuthUtil authUtil;

    private final GroupMessageStatusNotifier groupMessageStatusNotifier;

    @RabbitListener(queues = GROUP_MESSAGE_STATUS)
    public void processGroupMessageStatus(MessageWrapper<GroupMessageStatusSendNotifier> groupMessageStatusSendNotifier) {
        var request = groupMessageStatusSendNotifier.request();

        authUtil.createAuthenticationAndSetAuthenticationContext(
                groupMessageStatusSendNotifier.senderId(),
                groupMessageStatusSendNotifier.senderEmail(),
                groupMessageStatusSendNotifier.roles());

        var groupMessageUpdated = groupMessageService.updateGroupMessageStatus(
                request.groupMessageId(),
                request.messageStatus());

        groupMessageStatusNotifier.notifyNewGroupStatus(groupMessageUpdated);
    }


}
