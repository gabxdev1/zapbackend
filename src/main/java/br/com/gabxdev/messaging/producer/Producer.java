package br.com.gabxdev.messaging.producer;

import br.com.gabxdev.websocket.util.MessagingWrapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class Producer {

    private final RabbitTemplate rabbitTemplate;

    private final MessagingWrapperUtil messagingUtil;

    public <T> void sendMessage(T request, Principal principal, String exchange, String routingKey) {
        var requestWrapper = messagingUtil.createMessageWrapper(request, principal);

        rabbitTemplate.convertAndSend(exchange, routingKey, requestWrapper);
    }

    public void sendNotification(Principal principal, String exchange, String routingKey) {
        var requestWrapper = messagingUtil.createTriggerWrapper(principal);

        rabbitTemplate.convertAndSend(exchange, routingKey, requestWrapper);
    }
}
