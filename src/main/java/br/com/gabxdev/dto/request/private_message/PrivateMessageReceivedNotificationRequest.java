package br.com.gabxdev.dto.request.private_message;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PrivateMessageReceivedNotificationRequest(
        @org.hibernate.validator.constraints.UUID
        @NotNull
        UUID messageId
) {
}
