package br.com.gabxdev.notification.dto;

import br.com.gabxdev.model.enums.UserStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserPresenceStatusEvent(
        UserStatus status,

        Instant lastSeenAt
) {
}
