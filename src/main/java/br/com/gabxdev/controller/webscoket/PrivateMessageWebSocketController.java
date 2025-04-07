package br.com.gabxdev.controller.webscoket;

import br.com.gabxdev.dto.request.private_message.PrivateMessageReadNotificationRequest;
import br.com.gabxdev.dto.request.private_message.PrivateMessageReceivedNotificationRequest;
import br.com.gabxdev.dto.request.private_message.PrivateMessageSendRequest;
import br.com.gabxdev.messaging.producer.PrivateMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PrivateMessageWebSocketController {

    private final PrivateMessageProducer producer;

    @MessageMapping("/private-message")
    public void handlePrivateMessage(@Payload PrivateMessageSendRequest request, Principal principal) {
        producer.sendPrivateMessageEvent(request, principal);
    }

    @MessageMapping("/private-message-read")
    public void handleReadMessageEvent(@Payload PrivateMessageReadNotificationRequest request, Principal principal) {
        producer.sendPrivateMessageReadEvent(request, principal);
    }

    @MessageMapping("/private-message-received")
    public void handlePrivateMessageReceivedEvent(@Payload PrivateMessageReceivedNotificationRequest request, Principal principal) {
        producer.sendPrivateMessageReceivedEvent(request, principal);
    }
}
