package br.com.gabxdev.dto.request.privateMessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PrivateMessageSendRequest(

        @NotNull
        Long recipientId,

        @NotBlank
        String content
) {
}
