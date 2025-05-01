package br.com.gabxdev.messaging.consumer.group;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.dto.request.group_message.GroupMessageSendRequest;
import br.com.gabxdev.mapper.GroupMessageMapper;
import br.com.gabxdev.messaging.wrapper.MessageWrapper;
import br.com.gabxdev.service.group_message.GroupMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static br.com.gabxdev.config.RabbitMQConfig.QueueNames.GROUP_MESSAGE;

@Component
@RequiredArgsConstructor
public class GroupMessageConsumer {

    private final GroupMessageService groupMessageService;

    private final AuthUtil authUtil;

    private final GroupMessageMapper groupMessageMapper;

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = GROUP_MESSAGE)
    public void processGroupMessage(MessageWrapper<GroupMessageSendRequest> messageWrapper) {
        var request = messageWrapper.request();


        authUtil.createAuthenticationAndSetAuthenticationContext(
                messageWrapper.senderId(),
                messageWrapper.senderEmail(),
                messageWrapper.roles());

        var message = groupMessageService.save(request);

        var response = groupMessageMapper.toGroupMessageResponse(message);

        messagingTemplate.convertAndSend(
                "/topic/group-" + message.getGroup().getId(),
                response
        );
    }


}
