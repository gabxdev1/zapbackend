package br.com.gabxdev.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PRIVATE_MESSAGE_QUEUE = "private.message.queue";

    public static final String PRIVATE_MESSAGE_READ_QUEUE = "private.message.read.queue";

    public static final String PRIVATE_MESSAGE_RECEIVED_QUEUE = "private.message.received.queue";

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
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
