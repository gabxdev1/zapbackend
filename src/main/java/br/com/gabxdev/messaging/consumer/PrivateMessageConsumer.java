package br.com.gabxdev.messaging.consumer;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.config.RabbitMQConfig;
import br.com.gabxdev.dto.request.privateMessage.PrivateMessageReadNotificationRequest;
import br.com.gabxdev.dto.request.privateMessage.PrivateMessageReceivedNotificationRequest;
import br.com.gabxdev.dto.request.privateMessage.PrivateMessageSendRequest;
import br.com.gabxdev.mapper.PrivateMessageMapper;
import br.com.gabxdev.messaging.wrapper.MessageWrapper;
import br.com.gabxdev.model.enums.MessageStatus;
import br.com.gabxdev.service.PrivateMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrivateMessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    private final PrivateMessageMapper mapper;

    private final PrivateMessageService service;

    private final AuthUtil authUtil;

    @RabbitListener(queues = RabbitMQConfig.PRIVATE_MESSAGE_QUEUE)
    public void consumeMessage(MessageWrapper<PrivateMessageSendRequest> messageWrapper) {
        var request = messageWrapper.request();

        authUtil.createAuthenticationAndSetAuthenticationContext(
                messageWrapper.senderId(),
                messageWrapper.senderEmail(),
                messageWrapper.roles());

        var message = service.savePrivateMessage(request);

        var response = mapper.toPrivateMessageSendResponse(message);

        messagingTemplate.convertAndSendToUser(
                response.recipient().getEmail(),
                "/queue/messages",
                response
        );
    }

    @RabbitListener(queues = RabbitMQConfig.PRIVATE_MESSAGE_READ_QUEUE)
    public void processReadMessage(MessageWrapper<PrivateMessageReadNotificationRequest> messageWrapper) {
        var request = messageWrapper.request();

        var privateMessageUpdated = service.updatePrivateMessageStatusSafely(request.messageId(), MessageStatus.READ);

        var senderEmail = privateMessageUpdated.getSender().getEmail();

        var response = mapper.toPrivateMessageReadResponse(privateMessageUpdated);

        messagingTemplate.convertAndSendToUser(
                senderEmail,
                "/queue/messages/status",
                response
        );
    }

    @RabbitListener(queues = RabbitMQConfig.PRIVATE_MESSAGE_RECEIVED_QUEUE)
    public void processReceivedMessage(MessageWrapper<PrivateMessageReceivedNotificationRequest> messageWrapper) {
        var request = messageWrapper.request();

        var privateMessageUpdated = service.updatePrivateMessageStatusSafely(request.messageId(), MessageStatus.RECEIVED);

        var senderEmail = privateMessageUpdated.getSender().getEmail();

        var response = mapper.toPrivateMessageReadResponse(privateMessageUpdated);

        messagingTemplate.convertAndSendToUser(
                senderEmail,
                "/queue/messages/status",
                response
        );
    }
}
