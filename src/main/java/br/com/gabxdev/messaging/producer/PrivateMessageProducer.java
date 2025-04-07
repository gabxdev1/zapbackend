package br.com.gabxdev.messaging.producer;

import br.com.gabxdev.model.PrivateMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivateMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(PrivateMessage privateMessage) {
        rabbitTemplate.convertAndSend("chat.exchange", "chat.route", privateMessage);
    }
}
