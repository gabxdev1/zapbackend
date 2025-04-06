package br.com.gabxdev.response.user;

import br.com.gabxdev.model.enums.UserStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserStatusNotifyResponse(
        Long userId,

        UserStatus status,

        Instant lastSeen
) {
}
