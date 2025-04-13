package br.com.gabxdev.notification.listener;

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

import static br.com.gabxdev.config.RabbitMQConfig.Exchanges.DIRECT_USER_EVENTS;
import static br.com.gabxdev.config.RabbitMQConfig.RoutingKeys.USER_PRESENCE_CHANGE;

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

        producer.sendMessage(request, event.getUser(), DIRECT_USER_EVENTS, USER_PRESENCE_CHANGE);
    }


    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        var request = UserPresenceStatusEvent.builder()
                .status(UserStatus.OFFLINE)
                .lastSeenAt(Instant.now())
                .build();

        var email = event.getUser().getName();

        log.debug("WebSocket disconnected - UserEmail: {}, LastSeenAt: {}", email, request.lastSeenAt());

        producer.sendMessage(request, event.getUser(), DIRECT_USER_EVENTS, USER_PRESENCE_CHANGE);
    }
}
