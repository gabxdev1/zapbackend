package br.com.gabxdev.dto.response.group_message;

import br.com.gabxdev.dto.response.audit.AuditFullDetailsResponse;
import br.com.gabxdev.model.enums.MessageStatus;

import java.util.UUID;

public record GroupMessageResponse(
        Long groupId,

        UUID messageId,

        String content,

        MessageStatus status,

        AuditFullDetailsResponse audit
) {
}
