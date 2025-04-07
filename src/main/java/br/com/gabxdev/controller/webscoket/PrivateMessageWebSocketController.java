package br.com.gabxdev.controller.webscoket;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.config.RabbitMQConfig;
import br.com.gabxdev.dto.request.privateMessage.PrivateMessageSendRequest;
import br.com.gabxdev.messaging.wrapper.PrivateMessageWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PrivateMessageWebSocketController {

    private final RabbitTemplate rabbitTemplate;

    private final AuthUtil authUtil;

    @MessageMapping("/private-message")
    public void handlePrivateMessage(@Payload PrivateMessageSendRequest request, Principal principal) {
        authUtil.setAuthenticationContext(principal);

        var currentUser = authUtil.getCurrentUser();

        var roleList = currentUser.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var requestWrapper = PrivateMessageWrapper.builder()
                .request(request)
                .senderId(currentUser.getId())
                .senderEmail(currentUser.getEmail())
                .roles(roleList)
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConfig.PRIVATE_MESSAGE_QUEUE, requestWrapper);
    }

//    @MessageMapping("/private-message-read")
//    public void sendPrivateMessageReadNotificationAndSave(@Payload MessageReadNotificationRequest request, Principal principal) {
//        authUtil.setAuthenticationContext(principal);
//
//        var privateMessageUpdated = service.updatePrivateMessageStatusAndReadAt(request.messageId(), MessageStatus.READ);
//
//        var senderEmail = privateMessageUpdated.getSender().getEmail();
//
//        var response = mapper.toPrivateMessageReadResponse(privateMessageUpdated);
//
//        messagingTemplate.convertAndSendToUser(
//                senderEmail,
//                "/queue/messages/status",
//                response
//        );
//    }
//
//    @MessageMapping("/private-message-re")
//    public void sendPrivateMessageReceiveidNotificationAndSave(@Payload MessageReadNotificationRequest request, Principal principal) {
//        authUtil.setAuthenticationContext(principal);
//
//        var privateMessageUpdated = service.updatePrivateMessageStatusAndReadAt(request.messageId(), MessageStatus.READ);
//
//        var senderEmail = privateMessageUpdated.getSender().getEmail();
//
//        var response = mapper.toPrivateMessageReadResponse(privateMessageUpdated);
//
//        messagingTemplate.convertAndSendToUser(
//                senderEmail,
//                "/queue/messages/status",
//                response
//        );
//    }
}
