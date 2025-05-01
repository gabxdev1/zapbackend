package br.com.gabxdev.messaging.producer;

import br.com.gabxdev.model.enums.UserStatus;
import br.com.gabxdev.notification.dto.UserPresenceStatusEvent;
import br.com.gabxdev.websocket.util.MessagingWrapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.Instant;

import static br.com.gabxdev.config.RabbitMQConfig.Exchanges.DIRECT_USER_EVENTS;
import static br.com.gabxdev.config.RabbitMQConfig.RoutingKeys.USER_PRESENCE_CHANGE;

@Component
@RequiredArgsConstructor
public class Producer {

    private final RabbitTemplate rabbitTemplate;

    private final MessagingWrapperUtil messagingUtil;

    public <T> void sendMessage(T request, Principal principal, String exchange, String routingKey) {
        var requestWrapper = messagingUtil.createMessageWrapper(request, principal);

        rabbitTemplate.convertAndSend(exchange, routingKey, requestWrapper);
    }

    public <T> void sendMessage(T request, String exchange, String routingKey) {
        var requestWrapper = messagingUtil.createMessageWrapper(request);

        rabbitTemplate.convertAndSend(exchange, routingKey, requestWrapper);
    }

    public void sessionSyncNotification(Principal principal, String exchange, String routingKey) {
        var requestWrapper = messagingUtil.createTriggerWrapper(principal);

        rabbitTemplate.convertAndSend(exchange, routingKey, requestWrapper);

        var request = UserPresenceStatusEvent.builder()
                .status(UserStatus.ONLINE)
                .lastSeenAt(Instant.now())
                .build();

        this.sendMessage(request, principal, DIRECT_USER_EVENTS, USER_PRESENCE_CHANGE);
    }
}
