package br.com.gabxdev.messaging.consumer;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.dto.request.private_message.PrivateMessageReadNotificationRequest;
import br.com.gabxdev.dto.request.private_message.PrivateMessageReceivedNotificationRequest;
import br.com.gabxdev.dto.request.private_message.PrivateMessageSendRequest;
import br.com.gabxdev.mapper.PrivateMessageMapper;
import br.com.gabxdev.messaging.wrapper.MessageWrapper;
import br.com.gabxdev.model.enums.MessageStatus;
import br.com.gabxdev.service.private_message.PrivateMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static br.com.gabxdev.config.RabbitMQConfig.QueueNames.*;

@Component
@RequiredArgsConstructor
public class PrivateMessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    private final PrivateMessageMapper privateMessageMapper;

    private final PrivateMessageService service;

    private final AuthUtil authUtil;

    @RabbitListener(queues = PRIVATE_MESSAGE)
    public void processPrivateMessage(MessageWrapper<PrivateMessageSendRequest> messageWrapper) {
        var request = messageWrapper.request();

        authUtil.createAuthenticationAndSetAuthenticationContext(
                messageWrapper.senderId(),
                messageWrapper.senderEmail(),
                messageWrapper.roles());

        var message = service.savePrivateMessage(request);

        var response = privateMessageMapper.toPrivateMessageResponse(message);

        messagingTemplate.convertAndSendToUser(
                message.getRecipient().getEmail(),
                "/queue/messages",
                response
        );

        messagingTemplate.convertAndSendToUser(
                message.getSender().getEmail(),
                "/queue/status-private-message",
                response
        );
    }

    @RabbitListener(queues = PRIVATE_MESSAGE_READ)
    public void processReadMessage(MessageWrapper<PrivateMessageReadNotificationRequest> messageWrapper) {
        var request = messageWrapper.request();

        authUtil.createAuthenticationAndSetAuthenticationContext(
                messageWrapper.senderId(),
                messageWrapper.senderEmail(),
                messageWrapper.roles());

        service.updatePrivateMessageStatusSafely(request.messageId(), MessageStatus.READ).ifPresent(privateMessageUpdated -> {
            var senderEmail = privateMessageUpdated.getSender().getEmail();

            var response = privateMessageMapper.toPrivateMessageResponse(privateMessageUpdated);

            messagingTemplate.convertAndSendToUser(
                    senderEmail,
                    "/queue/status-private-message",
                    response
            );

            messagingTemplate.convertAndSendToUser(
                    privateMessageUpdated.getRecipient().getEmail(),
                    "/queue/status-private-message",
                    response
            );
        });
    }

    @RabbitListener(queues = PRIVATE_MESSAGE_RECEIVED)
    public void processReceivedMessage(MessageWrapper<PrivateMessageReceivedNotificationRequest> messageWrapper) {
        var request = messageWrapper.request();

        authUtil.createAuthenticationAndSetAuthenticationContext(
                messageWrapper.senderId(),
                messageWrapper.senderEmail(),
                messageWrapper.roles());

        service.updatePrivateMessageStatusSafely(request.messageId(), MessageStatus.RECEIVED).ifPresent(privateMessageUpdated -> {
            var senderEmail = privateMessageUpdated.getSender().getEmail();

            var response = privateMessageMapper.toPrivateMessageResponse(privateMessageUpdated);

            messagingTemplate.convertAndSendToUser(
                    senderEmail,
                    "/queue/status-private-message",
                    response
            );

            messagingTemplate.convertAndSendToUser(
                    privateMessageUpdated.getRecipient().getEmail(),
                    "/queue/status-private-message",
                    response
            );
        });
    }
}
