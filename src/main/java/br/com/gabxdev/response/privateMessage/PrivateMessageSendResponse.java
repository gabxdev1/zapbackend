package br.com.gabxdev.response.privateMessage;

import br.com.gabxdev.model.User;
import br.com.gabxdev.model.enums.MessageStatus;
import br.com.gabxdev.response.audit.AuditFullDetailsResponse;

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
