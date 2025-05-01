package br.com.gabxdev.dto.request.group_message;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GroupMessageStatusNotificationRequest(
        @NotNull
        @org.hibernate.validator.constraints.UUID
        UUID messageId
) {
}
