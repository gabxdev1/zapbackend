package br.com.gabxdev.notification.dto;

import br.com.gabxdev.model.enums.MessageStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record GroupMessageStatusSendNotifier(
        UUID groupMessageId,

        MessageStatus messageStatus
) {
}
