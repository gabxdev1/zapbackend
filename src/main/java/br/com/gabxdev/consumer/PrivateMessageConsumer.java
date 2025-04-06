package br.com.gabxdev.consumer;

import br.com.gabxdev.model.PrivateMessage;
import br.com.gabxdev.repository.PrivateMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrivateMessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    private final PrivateMessageRepository privateMessageRepository;

    @RabbitListener(queues = "chat.queue")
    public void listen(PrivateMessage privateMessage) {
        var message = privateMessageRepository.save(privateMessage);

        var recipientId = message.getRecipient().getId().toString();

        messagingTemplate.convertAndSendToUser(
                recipientId, "/queue/messages", message
        );
    }
}
