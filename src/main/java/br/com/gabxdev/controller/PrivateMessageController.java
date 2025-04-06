package br.com.gabxdev.controller;

import br.com.gabxdev.request.message.PrivateMessageSendRequest;
import br.com.gabxdev.service.PrivateMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PrivateMessageController {

    private final PrivateMessageService service;

    @MessageMapping("/chat/private.send")
    public void handleChatMessage(@Valid @Payload PrivateMessageSendRequest message, Principal principal) {
        service.sendPrivateMessage(message, principal);
    }
}
