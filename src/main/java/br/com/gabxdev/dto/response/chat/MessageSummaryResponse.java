package br.com.gabxdev.dto.response.chat;

import br.com.gabxdev.dto.response.audit.AuditFullDetailsResponse;
import br.com.gabxdev.model.enums.MessageStatus;

import java.time.Instant;
import java.util.UUID;

public record MessageSummaryResponse(
        UUID messageId,

        Long senderId,

        Long recipientId,

        String content,

        MessageStatus status,

        Instant readAt,

        Instant receivedAt,

        AuditFullDetailsResponse audit
) {
}
