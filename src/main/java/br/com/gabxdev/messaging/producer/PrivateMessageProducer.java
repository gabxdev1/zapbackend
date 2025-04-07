package br.com.gabxdev.messaging.producer;

import br.com.gabxdev.config.RabbitMQConfig;
import br.com.gabxdev.dto.request.privateMessage.PrivateMessageReadNotificationRequest;
import br.com.gabxdev.dto.request.privateMessage.PrivateMessageReceivedNotificationRequest;
import br.com.gabxdev.dto.request.privateMessage.PrivateMessageSendRequest;
import br.com.gabxdev.websocket.util.MessagingWrapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PrivateMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    private final MessagingWrapperUtil messagingUtil;

    public void sendPrivateMessageEvent(PrivateMessageSendRequest request, Principal principal) {
        var requestWrapper = messagingUtil.preparePrivateMessageWrapper(request, principal);

        rabbitTemplate.convertAndSend(RabbitMQConfig.PRIVATE_MESSAGE_QUEUE, requestWrapper);
    }

    public void sendPrivateMessageReadEvent(PrivateMessageReadNotificationRequest request, Principal principal) {
        var requestWrapper = messagingUtil.preparePrivateMessageWrapper(request, principal);

        rabbitTemplate.convertAndSend(RabbitMQConfig.PRIVATE_MESSAGE_READ_QUEUE, requestWrapper);
    }

    public void sendPrivateMessageReceivedEvent(PrivateMessageReceivedNotificationRequest request, Principal principal) {
        var requestWrapper = messagingUtil.preparePrivateMessageWrapper(request, principal);

        rabbitTemplate.convertAndSend(RabbitMQConfig.PRIVATE_MESSAGE_READ_QUEUE, requestWrapper);
    }
}
