package br.com.gabxdev.notification.listener;

import br.com.gabxdev.config.RabbitMQConfig;
import br.com.gabxdev.messaging.producer.Producer;
import br.com.gabxdev.model.enums.UserStatus;
import br.com.gabxdev.notification.dto.UserPresenceStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketPresenceEventListener {

    private final Producer producer;

    @EventListener
    public void handleWebSocketConnect(SessionConnectedEvent event) {
        var request = UserPresenceStatusEvent.builder()
                .status(UserStatus.ONLINE)
                .lastSeenAt(Instant.now())
                .build();

        var email = event.getUser().getName();

        log.debug("WebSocket connected - UserEmail: {}", email);

        producer.sendMessage(request, event.getUser(), RabbitMQConfig.PRESENCE_CHANGE_QUEUE);
    }


    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        var request = UserPresenceStatusEvent.builder()
                .status(UserStatus.OFFLINE)
                .lastSeenAt(Instant.now())
                .build();

        var email = event.getUser().getName();

        log.debug("WebSocket disconnected - UserEmail: {}, LastSeenAt: {}", email, request.lastSeenAt());

        producer.sendMessage(request, event.getUser(), RabbitMQConfig.PRESENCE_CHANGE_QUEUE);
    }
}
