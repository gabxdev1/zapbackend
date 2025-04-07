package br.com.gabxdev.dto.response.privateMessage;

import br.com.gabxdev.dto.response.audit.AuditFullDetailsResponse;
import br.com.gabxdev.model.User;
import br.com.gabxdev.model.enums.MessageStatus;

import java.time.Instant;

public record PrivateMessageSendResponse(
        Long id,

        User recipient,

        String content,

        Instant readAt,

        Instant receivedAt,

        MessageStatus status,

        AuditFullDetailsResponse audit
) {
}
