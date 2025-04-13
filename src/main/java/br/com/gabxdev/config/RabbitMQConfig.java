package br.com.gabxdev.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static class QueueNames {
        public static final String PRIVATE_MESSAGE = "queue.chat.private.message";
        public static final String PRIVATE_MESSAGE_READ = "queue.chat.private.message.read";
        public static final String PRIVATE_MESSAGE_RECEIVED = "queue.chat.private.message.received";
        public static final String USER_PRESENCE_CHANGE = "queue.user.presence.change";
    }

    public static class RoutingKeys {
        public static final String PRIVATE_MESSAGE = "routing.chat.private.message";
        public static final String PRIVATE_MESSAGE_READ = "routing.chat.private.message.read";
        public static final String PRIVATE_MESSAGE_RECEIVED = "routing.chat.private.message.received";
        public static final String USER_PRESENCE_CHANGE = "routing.user.presence.change";
    }

    public static class Exchanges {
        public static final String DIRECT_CHAT_EVENTS = "exchange.chat.events";
        public static final String DIRECT_USER_EVENTS = "exchange.user.events";
    }
    
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
    public DirectExchange directExchangeChatEvents() {
        return ExchangeBuilder.directExchange(Exchanges.DIRECT_CHAT_EVENTS)
                .durable(true)
                .build();
    }

    @Bean
    public DirectExchange directExchangeUserEvents() {
        return ExchangeBuilder.directExchange(Exchanges.DIRECT_USER_EVENTS)
                .durable(true)
                .build();
    }

    @Bean
    public Queue privateMessageQueue() {
        return QueueBuilder.durable(QueueNames.PRIVATE_MESSAGE).build();
    }

    @Bean
    public Queue privateMessageReadQueue() {
        return QueueBuilder.durable(QueueNames.PRIVATE_MESSAGE_READ).build();
    }

    @Bean
    public Queue privateMessageReceivedQueue() {
        return QueueBuilder.durable(QueueNames.PRIVATE_MESSAGE_RECEIVED).build();
    }

    @Bean
    public Queue userPresenceChangeQueue() {
        return QueueBuilder.durable(QueueNames.USER_PRESENCE_CHANGE).build();
    }

    @Bean
    public Binding privateMessageBinding() {
        return BindingBuilder
                .bind(privateMessageQueue())
                .to(directExchangeChatEvents())
                .with(RoutingKeys.PRIVATE_MESSAGE);
    }

    @Bean
    public Binding privateMessageReadBinding() {
        return BindingBuilder
                .bind(privateMessageReadQueue())
                .to(directExchangeChatEvents())
                .with(RoutingKeys.PRIVATE_MESSAGE_READ);
    }

    @Bean
    public Binding privateMessageReceivedBinding() {
        return BindingBuilder
                .bind(privateMessageReceivedQueue())
                .to(directExchangeChatEvents())
                .with(RoutingKeys.PRIVATE_MESSAGE_RECEIVED);
    }

    @Bean
    public Binding userPresenceChangeBinding() {
        return BindingBuilder
                .bind(userPresenceChangeQueue())
                .to(directExchangeUserEvents())
                .with(RoutingKeys.USER_PRESENCE_CHANGE);
    }
}
