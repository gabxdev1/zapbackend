package br.com.gabxdev.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static class QueueNames {
        public static final String PRIVATE_MESSAGE = "queue.chat.private.message";
        public static final String GROUP_MESSAGE = "queue.chat.group.message";
        public static final String PRIVATE_MESSAGE_READ = "queue.chat.private.message.read";
        public static final String GROUP_MESSAGE_READ = "queue.chat.group.message.read";
        public static final String PRIVATE_MESSAGE_RECEIVED = "queue.chat.private.message.received";
        public static final String GROUP_MESSAGE_RECEIVED = "queue.chat.group.message.received";
        public static final String USER_PRESENCE_CHANGE = "queue.user.presence.change";
        public static final String MESSAGE_DEAD_LETTER = "queue.chat.message.dead-letter";
        public static final String SESSION_SYNC = "queue.trigger.session.sync";
        public static final String GROUP_MESSAGE_STATUS = "queue.chat.group.message.status";
        public static final String GROUP_CREATED_NOTIFICATION = "queue.group.created.notification";
        public static final String GROUP_NEW_MEMBER_NOTIFICATION = "queue.group.new-member.notification";
    }

    public static class RoutingKeys {
        public static final String PRIVATE_MESSAGE = "routing.chat.private.message";
        public static final String GROUP_MESSAGE = "routing.chat.group.message";
        public static final String GROUP_MESSAGE_STATUS = "routing.chat.group.message.status";
        public static final String SESSION_SYNC = "routing.trigger.session.sync";
        public static final String PRIVATE_MESSAGE_READ = "routing.chat.private.message.read";
        public static final String GROUP_MESSAGE_READ = "routing.chat.group.message.read";
        public static final String PRIVATE_MESSAGE_RECEIVED = "routing.chat.private.message.received";
        public static final String GROUP_MESSAGE_RECEIVED = "routing.chat.group.message.received";
        public static final String USER_PRESENCE_CHANGE = "routing.user.presence.change";
        public static final String MESSAGE_DEAD_LETTER = "routing.chat.message.dead-letter";
        public static final String GROUP_CREATED_NOTIFICATION = "routing.group.created";
        public static final String GROUP_NEW_MEMBER_NOTIFICATION = "routing.group.new-member";
    }

    public static class Exchanges {
        public static final String DIRECT_CHAT_EVENTS = "exchange.chat.events";
        public static final String DIRECT_USER_EVENTS = "exchange.user.events";
        public static final String TOPIC_GROUP_EVENTS = "exchange.group.events";
        public static final String DIRECT_CHAT_DEAD_LETTER = "exchange.chat.dead-letter";
        public static final String DIRECT_TRIGGER_SESSION_SYNC = "exchange.trigger.session.sync";
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
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setDefaultRequeueRejected(false);
        // factory.setConcurrentConsumers(3);
        // factory.setMaxConcurrentConsumers(10);
        return factory;
    }

    @Bean
    public DirectExchange directExchangeChatDeadLetter() {
        return ExchangeBuilder.directExchange(Exchanges.DIRECT_CHAT_DEAD_LETTER)
                .durable(true)
                .build();
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
    public DirectExchange directExchangeTriggerEvents() {
        return ExchangeBuilder.directExchange(Exchanges.DIRECT_TRIGGER_SESSION_SYNC)
                .durable(true)
                .build();
    }

    @Bean
    public TopicExchange topicExchangeGroupEvents() {
        return ExchangeBuilder.topicExchange(Exchanges.TOPIC_GROUP_EVENTS)
                .durable(true)
                .build();
    }

    @Bean
    public Queue messageDeadLetterQueue() {
        return QueueBuilder.durable(QueueNames.MESSAGE_DEAD_LETTER).build();
    }

    @Bean
    public Queue privateMessageQueue() {
        return QueueBuilder.durable(QueueNames.PRIVATE_MESSAGE)
                .deadLetterExchange(Exchanges.DIRECT_CHAT_DEAD_LETTER)
                .deadLetterRoutingKey(RoutingKeys.MESSAGE_DEAD_LETTER)
                .build();
    }

    @Bean
    public Queue groupMessageQueue() {
        return QueueBuilder.durable(QueueNames.GROUP_MESSAGE)
                .deadLetterExchange(Exchanges.DIRECT_CHAT_DEAD_LETTER)
                .deadLetterRoutingKey(RoutingKeys.MESSAGE_DEAD_LETTER)
                .build();
    }

    @Bean
    public Queue groupMessageStatusQueue() {
        return QueueBuilder.durable(QueueNames.GROUP_MESSAGE_STATUS)
                .build();
    }

    @Bean
    public Queue sessionSyncQueue() {
        return QueueBuilder.durable(QueueNames.SESSION_SYNC).build();
    }

    @Bean
    public Queue privateMessageReadQueue() {
        return QueueBuilder.durable(QueueNames.PRIVATE_MESSAGE_READ).build();
    }

    @Bean
    public Queue groupMessageReadQueue() {
        return QueueBuilder.durable(QueueNames.GROUP_MESSAGE_READ).build();
    }

    @Bean
    public Queue privateMessageReceivedQueue() {
        return QueueBuilder.durable(QueueNames.PRIVATE_MESSAGE_RECEIVED).build();
    }

    @Bean
    public Queue groupMessageReceivedQueue() {
        return QueueBuilder.durable(QueueNames.GROUP_MESSAGE_RECEIVED).build();
    }

    @Bean
    public Queue userPresenceChangeQueue() {
        return QueueBuilder.durable(QueueNames.USER_PRESENCE_CHANGE).build();
    }

    @Bean
    public Queue groupCreatedNotificationQueue() {
        return QueueBuilder.durable(QueueNames.GROUP_CREATED_NOTIFICATION).build();
    }

    @Bean
    public Queue groupNewMemberdNotificationQueue() {
        return QueueBuilder.durable(QueueNames.GROUP_NEW_MEMBER_NOTIFICATION).build();
    }

    @Bean
    public Binding messageDeadLetterBinding() {
        return BindingBuilder
                .bind(messageDeadLetterQueue())
                .to(directExchangeChatDeadLetter())
                .with(RoutingKeys.MESSAGE_DEAD_LETTER);
    }

    @Bean
    public Binding privateMessageBinding() {
        return BindingBuilder
                .bind(privateMessageQueue())
                .to(directExchangeChatEvents())
                .with(RoutingKeys.PRIVATE_MESSAGE);
    }

    @Bean
    public Binding groupMessageBinding() {
        return BindingBuilder
                .bind(groupMessageQueue())
                .to(directExchangeChatEvents())
                .with(RoutingKeys.GROUP_MESSAGE);
    }

    @Bean
    public Binding groupMessageStatusBinding() {
        return BindingBuilder
                .bind(groupMessageStatusQueue())
                .to(directExchangeChatEvents())
                .with(RoutingKeys.GROUP_MESSAGE_STATUS);
    }

    @Bean
    public Binding sessionSyncBinding() {
        return BindingBuilder
                .bind(sessionSyncQueue())
                .to(directExchangeTriggerEvents())
                .with(RoutingKeys.SESSION_SYNC);
    }

    @Bean
    public Binding privateMessageReadBinding() {
        return BindingBuilder
                .bind(privateMessageReadQueue())
                .to(directExchangeChatEvents())
                .with(RoutingKeys.PRIVATE_MESSAGE_READ);
    }

    @Bean
    public Binding groupMessageReadBinding() {
        return BindingBuilder
                .bind(groupMessageReadQueue())
                .to(directExchangeChatEvents())
                .with(RoutingKeys.GROUP_MESSAGE_READ);
    }

    @Bean
    public Binding privateMessageReceivedBinding() {
        return BindingBuilder
                .bind(privateMessageReceivedQueue())
                .to(directExchangeChatEvents())
                .with(RoutingKeys.PRIVATE_MESSAGE_RECEIVED);
    }

    @Bean
    public Binding groupMessageReceivedBinding() {
        return BindingBuilder
                .bind(groupMessageReceivedQueue())
                .to(directExchangeChatEvents())
                .with(RoutingKeys.GROUP_MESSAGE_RECEIVED);
    }

    @Bean
    public Binding userPresenceChangeBinding() {
        return BindingBuilder
                .bind(userPresenceChangeQueue())
                .to(directExchangeUserEvents())
                .with(RoutingKeys.USER_PRESENCE_CHANGE);
    }

    @Bean
    public Binding groupCreatedNotificationBinding() {
        return BindingBuilder
                .bind(groupCreatedNotificationQueue())
                .to(topicExchangeGroupEvents())
                .with(RoutingKeys.GROUP_CREATED_NOTIFICATION);
    }

    @Bean
    public Binding groupNewMemberNotificationBinding() {
        return BindingBuilder
                .bind(groupNewMemberdNotificationQueue())
                .to(topicExchangeGroupEvents())
                .with(RoutingKeys.GROUP_NEW_MEMBER_NOTIFICATION);
    }
}
