package br.com.gabxdev.dto.response.private_message;

import br.com.gabxdev.model.enums.MessageStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record MessageStatusNotification(
        UUID messageId,

        MessageStatus status,

        Instant timestamp
) {
}
