package br.com.gabxdev.controller.webscoket;

import br.com.gabxdev.dto.request.private_message.PrivateMessageReadNotificationRequest;
import br.com.gabxdev.dto.request.private_message.PrivateMessageReceivedNotificationRequest;
import br.com.gabxdev.dto.request.private_message.PrivateMessageSendRequest;
import br.com.gabxdev.messaging.producer.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static br.com.gabxdev.config.RabbitMQConfig.Exchanges.DIRECT_CHAT_EVENTS;
import static br.com.gabxdev.config.RabbitMQConfig.Exchanges.DIRECT_TRIGGER_SESSION_SYNC;
import static br.com.gabxdev.config.RabbitMQConfig.RoutingKeys.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PrivateMessageWebSocketController {

    private final Producer producer;

    @MessageMapping("/private-message")
    public void handlePrivateMessageEvent(@Payload PrivateMessageSendRequest request, Principal principal) {
        producer.sendMessage(request, principal, DIRECT_CHAT_EVENTS, PRIVATE_MESSAGE);
    }

    @MessageMapping("/private-message-read")
    public void handleGroupMessageReadEvent(@Payload PrivateMessageReadNotificationRequest request, Principal principal) {
        producer.sendMessage(request, principal, DIRECT_CHAT_EVENTS, PRIVATE_MESSAGE_READ);
    }

    @MessageMapping("/private-message-received")
    public void handlePrivateMessageReceivedEvent(@Payload PrivateMessageReceivedNotificationRequest request, Principal principal) {
        producer.sendMessage(request, principal, DIRECT_CHAT_EVENTS, PRIVATE_MESSAGE_RECEIVED);
    }

    @MessageMapping("/session-sync")
    public void handleTriggerOldMessagesEvent(Principal principal) {
        log.debug("session-sync: '{}'", principal.getName());

        producer.sessionSyncNotification(principal, DIRECT_TRIGGER_SESSION_SYNC, SESSION_SYNC);
    }
}
