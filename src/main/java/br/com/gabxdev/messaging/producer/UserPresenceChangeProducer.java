package br.com.gabxdev.messaging.producer;

import br.com.gabxdev.config.RabbitMQConfig;
import br.com.gabxdev.notification.dto.UserPresenceStatusEvent;
import br.com.gabxdev.websocket.util.MessagingWrapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class UserPresenceChangeProducer {

    private final RabbitTemplate rabbitTemplate;

    private final MessagingWrapperUtil messagingUtil;

    public void sendPresenceChange(UserPresenceStatusEvent event, Principal principal) {
        var eventRequest = messagingUtil.preparePrivateMessageWrapper(event, principal);

        rabbitTemplate.convertAndSend(RabbitMQConfig.PRESENCE_CHANGE_QUEUE, eventRequest);
    }
}
