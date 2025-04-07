package br.com.gabxdev.notification.listener;

import br.com.gabxdev.messaging.producer.UserPresenceChangeProducer;
import br.com.gabxdev.model.enums.UserStatus;
import br.com.gabxdev.notification.dto.UserPresenceStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketPresenceEventListener {

    private final UserPresenceChangeProducer producer;

    @EventListener
    public void handleWebSocketConnect(SessionConnectEvent event) {
        var request = UserPresenceStatusEvent.builder()
                .status(UserStatus.ONLINE)
                .lastSeenAt(Instant.now())
                .build();

        var email = event.getUser().getName();

        log.debug("WebSocket connected - UserEmail: {}", email);

        producer.sendPresenceChange(request, event.getUser());
    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        var request = UserPresenceStatusEvent.builder()
                .status(UserStatus.OFFLINE)
                .lastSeenAt(Instant.now())
                .build();

        var email = event.getUser().getName();

        log.debug("WebSocket disconnected - UserEmail: {}, LastSeenAt: {}", email, request.lastSeenAt());

        producer.sendPresenceChange(request, event.getUser());
    }
}
