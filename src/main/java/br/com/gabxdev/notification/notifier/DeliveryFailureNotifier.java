package br.com.gabxdev.notification.notifier;

import br.com.gabxdev.notification.dto.DeliveryFailureResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryFailureNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifySender(String email, UUID messageId) {
        var payload = DeliveryFailureResponse.builder()
                .messageId(messageId)
                .message("The message could not be delivered.")
                .build();
        log.debug("{}", payload);

        messagingTemplate.convertAndSendToUser(
                email,
                "/queue/message.delivery.failed",
                payload
        );
    }
}
