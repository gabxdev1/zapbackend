package br.com.gabxdev.dto.request.private_message;

import java.util.UUID;

public record PrivateMessageReadNotificationRequest(
        UUID messageId
) {
}
