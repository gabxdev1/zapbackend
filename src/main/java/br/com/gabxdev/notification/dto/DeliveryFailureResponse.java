package br.com.gabxdev.notification.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DeliveryFailureResponse(
        UUID messageId,

        String message
) {
}
