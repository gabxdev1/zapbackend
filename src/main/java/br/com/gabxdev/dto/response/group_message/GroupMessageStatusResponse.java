package br.com.gabxdev.dto.response.group_message;

import br.com.gabxdev.model.enums.MessageStatus;

import java.time.Instant;
import java.util.UUID;

public record GroupMessageStatusResponse(
        Long userId,

        Long senderId,

        String firstName,

        String lastName,

        UUID messageId,

        Long groupId,

        MessageStatus messageStatus,

        Instant receivedAt,

        Instant readAt
) {
}
