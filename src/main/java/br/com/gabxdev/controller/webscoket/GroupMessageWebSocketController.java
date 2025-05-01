package br.com.gabxdev.controller.webscoket;

import br.com.gabxdev.dto.request.group_message.GroupMessageSendRequest;
import br.com.gabxdev.dto.request.group_message.GroupMessageStatusNotificationRequest;
import br.com.gabxdev.messaging.producer.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static br.com.gabxdev.config.RabbitMQConfig.Exchanges.DIRECT_CHAT_EVENTS;
import static br.com.gabxdev.config.RabbitMQConfig.RoutingKeys.*;

@Controller
@RequiredArgsConstructor
public class GroupMessageWebSocketController {

    private final Producer producer;

    @MessageMapping("/group-message")
    public void handleGroupMessageEvent(@Payload GroupMessageSendRequest message, Principal principal) {
        producer.sendMessage(message, principal, DIRECT_CHAT_EVENTS, GROUP_MESSAGE);
    }

    @MessageMapping("/group-message-read")
    public void handleGroupMessageReadEvent(@Payload GroupMessageStatusNotificationRequest request, Principal principal) {
        producer.sendMessage(request, principal, DIRECT_CHAT_EVENTS, GROUP_MESSAGE_READ);
    }

    @MessageMapping("/group-message-received")
    public void handleGroupMessageReceivedEvent(@Payload GroupMessageStatusNotificationRequest request, Principal principal) {
        producer.sendMessage(request, principal, DIRECT_CHAT_EVENTS, GROUP_MESSAGE_RECEIVED);
    }
}
