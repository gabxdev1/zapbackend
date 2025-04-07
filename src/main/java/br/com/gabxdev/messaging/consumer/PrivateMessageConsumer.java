package br.com.gabxdev.messaging.consumer;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.config.RabbitMQConfig;
import br.com.gabxdev.mapper.PrivateMessageMapper;
import br.com.gabxdev.messaging.wrapper.PrivateMessageWrapper;
import br.com.gabxdev.service.PrivateMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrivateMessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    private final PrivateMessageMapper mapper;

    private final PrivateMessageService service;

    private final AuthUtil authUtil;

    @RabbitListener(queues = RabbitMQConfig.PRIVATE_MESSAGE_QUEUE)
    public void consumeMessage(PrivateMessageWrapper privateMessageWrapper) {
        var request = privateMessageWrapper.request();

        authUtil.createAuthenticationAndSetAuthenticationContext(
                privateMessageWrapper.senderId(),
                privateMessageWrapper.senderEmail(),
                privateMessageWrapper.roles());

        var message = service.savePrivateMessage(request);

        var response = mapper.toPrivateMessageSendResponse(message);

        messagingTemplate.convertAndSendToUser(
                response.recipient().getEmail(),
                "/queue/messages",
                response
        );
    }
}
