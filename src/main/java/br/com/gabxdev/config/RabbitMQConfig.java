package br.com.gabxdev.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PRIVATE_MESSAGE_QUEUE = "private.message.queue";

    public static final String PRIVATE_MESSAGE_READ_QUEUE = "private.message.read.queue";

    public static final String PRIVATE_MESSAGE_RECEIVED_QUEUE = "private.message.received.queue";

    public static final String PRESENCE_CHANGE_QUEUE = "presence.change.queue";

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        return rabbitTemplate;
    }

    @Bean
    public Queue privateMessageQueue() {
        return new Queue(PRIVATE_MESSAGE_QUEUE, true);
    }

    @Bean
    public Queue privateMessageReadQueue() {
        return new Queue(PRIVATE_MESSAGE_READ_QUEUE, true);
    }

    @Bean
    public Queue privateMessageReceivedQueue() {
        return new Queue(PRIVATE_MESSAGE_RECEIVED_QUEUE, true);
    }

    @Bean
    public Queue presenceChangeQueue() {
        return new Queue(PRESENCE_CHANGE_QUEUE, true);
    }


}
