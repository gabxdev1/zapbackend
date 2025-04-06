package br.com.gabxdev.controller;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.mapper.PrivateMessageMapper;
import br.com.gabxdev.model.enums.MessageStatus;
import br.com.gabxdev.request.privateMessage.MessageReadNotificationRequest;
import br.com.gabxdev.request.privateMessage.PrivateMessageSendRequest;
import br.com.gabxdev.response.privateMessage.PrivateMessageReadResponse;
import br.com.gabxdev.service.PrivateMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PrivateMessageController {

    private final PrivateMessageService service;

    private final SimpMessagingTemplate messagingTemplate;

    private final AuthUtil authUtil;

    private final PrivateMessageMapper mapper;

    @MessageMapping("/private-message")
    public void sendPrivateMessageNotificationAndSave(@Payload PrivateMessageSendRequest request, Principal principal) {
        authUtil.setAuthenticationContext(principal);

        var messageSaved = service.savePrivateMessage(request);

        var response = mapper.toPrivateMessageSendResponse(messageSaved);

        messagingTemplate.convertAndSendToUser(
                response.recipient().getEmail(),
                "/queue/messages",
                response
        );
    }

    @MessageMapping("/private-message-read")
    public void sendPrivateMessageReadNotificationAndSave(@Payload MessageReadNotificationRequest request, Principal principal) {
        authUtil.setAuthenticationContext(principal);

        var privateMessageUpdated = service.updatePrivateMessageStatusAndReadAt(request.messageId(), MessageStatus.READ);

        var senderEmail = privateMessageUpdated.getSender().getEmail();

        var response = mapper.toPrivateMessageReadResponse(privateMessageUpdated);

        messagingTemplate.convertAndSendToUser(
                senderEmail,
                "/queue/messages/status",
                response
        );
    }

    @MessageMapping("/private-message-re")
    public void sendPrivateMessageReceiveidNotificationAndSave(@Payload MessageReadNotificationRequest request, Principal principal) {
        authUtil.setAuthenticationContext(principal);

        var privateMessageUpdated = service.updatePrivateMessageStatusAndReadAt(request.messageId(), MessageStatus.READ);

        var senderEmail = privateMessageUpdated.getSender().getEmail();

        var response = mapper.toPrivateMessageReadResponse(privateMessageUpdated);

        messagingTemplate.convertAndSendToUser(
                senderEmail,
                "/queue/messages/status",
                response
        );
    }
}
