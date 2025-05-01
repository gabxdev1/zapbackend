package br.com.gabxdev.messaging.consumer.group;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.dto.request.group_message.GroupMessageStatusNotificationRequest;
import br.com.gabxdev.mapper.GroupMessageMapper;
import br.com.gabxdev.messaging.wrapper.MessageWrapper;
import br.com.gabxdev.service.group_message.GroupMessageStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static br.com.gabxdev.config.RabbitMQConfig.QueueNames.GROUP_MESSAGE_READ;

@Component
@RequiredArgsConstructor
public class GroupMessageReadConsumer {

    private final GroupMessageStatusService groupMessageStatusService;

    private final AuthUtil authUtil;

    private final GroupMessageMapper groupMessageMapper;

    private final SimpMessagingTemplate messagingTemplate;


    @RabbitListener(queues = GROUP_MESSAGE_READ)
    public void processReadMessage(MessageWrapper<GroupMessageStatusNotificationRequest> messageWrapper) {
        var request = messageWrapper.request();


        authUtil.createAuthenticationAndSetAuthenticationContext(
                messageWrapper.senderId(),
                messageWrapper.senderEmail(),
                messageWrapper.roles());

        groupMessageStatusService.markMessageAsRead(request.messageId()).ifPresent(groupMessage -> {
            var response = groupMessageMapper.toGroupMessageStatusResponse(groupMessage);

            messagingTemplate.convertAndSend(
                    "/topic/status-group-message-" + response.groupId(),
                    response);
        });
    }


}
