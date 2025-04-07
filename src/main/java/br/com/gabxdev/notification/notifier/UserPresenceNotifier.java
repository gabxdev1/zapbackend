package br.com.gabxdev.notification.notifier;

import br.com.gabxdev.model.enums.UserStatus;
import br.com.gabxdev.notification.dto.UserPresenceStatusResponse;
import br.com.gabxdev.service.user.UserRelationshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserPresenceNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    private final UserRelationshipService userRelationshipService;

    public void notifyUserPresenceChange(Long userId, UserStatus userStatus, Instant lastSeenAt) {
        log.debug("Notifying change of presence - ID: {}, Status: {}, LastSeenAt: {}",
                userId, userStatus, lastSeenAt);

        var relatedEmails = userRelationshipService.getRelatedUserEmails(userId);
        log.debug("Related users found: {}", relatedEmails);

        var payload = UserPresenceStatusResponse.builder()
                .userId(userId)
                .status(userStatus)
                .lastSeenAt(lastSeenAt)
                .build();

        relatedEmails.forEach(email -> {
            log.debug("Sending notification to: {}", email);

            messagingTemplate.convertAndSendToUser(
                    email,
                    "/queue/presence",
                    payload
            );
        });
    }

}
