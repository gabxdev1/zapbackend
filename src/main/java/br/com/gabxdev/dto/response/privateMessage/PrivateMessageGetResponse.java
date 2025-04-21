package br.com.gabxdev.dto.response.privateMessage;

import br.com.gabxdev.dto.response.audit.AuditFullDetailsResponse;
import br.com.gabxdev.dto.response.user.UserGetResponse;
import br.com.gabxdev.model.enums.MessageStatus;

import java.time.Instant;
import java.util.UUID;

public record PrivateMessageGetResponse(
        UUID messageId,

        Long senderId,

        UserGetResponse recipient,

        String content,

        MessageStatus status,

        Instant readAt,

        Instant receivedAt,

        AuditFullDetailsResponse audit
) {
}
