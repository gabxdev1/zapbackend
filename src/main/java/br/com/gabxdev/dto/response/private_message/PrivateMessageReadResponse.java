package br.com.gabxdev.dto.response.private_message;

import br.com.gabxdev.model.enums.MessageStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record PrivateMessageReadResponse(
        Long id,

        MessageStatus status,

        Instant readAt
) {
}
