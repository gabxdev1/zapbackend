package br.com.gabxdev.dto.response.privateMessage;

import br.com.gabxdev.dto.response.audit.AuditFullDetailsResponse;
import br.com.gabxdev.model.enums.MessageStatus;

import java.util.UUID;

public record PrivateMessageNotificationResponse(
        UUID messageId,

        Long senderId,

        String content,

        MessageStatus status,

        AuditFullDetailsResponse audit
) {
}
