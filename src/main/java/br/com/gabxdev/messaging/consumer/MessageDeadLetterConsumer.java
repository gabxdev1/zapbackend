package br.com.gabxdev.messaging.consumer;

import br.com.gabxdev.dto.request.private_message.PrivateMessageSendRequest;
import br.com.gabxdev.messaging.wrapper.MessageWrapper;
import br.com.gabxdev.notification.notifier.DeliveryFailureNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.gabxdev.config.RabbitMQConfig.QueueNames.MESSAGE_DEAD_LETTER;

@RequiredArgsConstructor
@Component
public class MessageDeadLetterConsumer {

    private final DeliveryFailureNotifier deliveryFailureNotifier;

    @RabbitListener(queues = MESSAGE_DEAD_LETTER)
    public void handlePrivateMessage(MessageWrapper<PrivateMessageSendRequest> messageWrapper) {
        var senderEmail = messageWrapper.senderEmail();
        var messageId = messageWrapper.request().messageId();


        deliveryFailureNotifier.notifySender(senderEmail, messageId);
    }
}
