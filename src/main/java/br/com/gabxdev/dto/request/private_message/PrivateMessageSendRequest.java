package br.com.gabxdev.dto.request.private_message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PrivateMessageSendRequest(

        @NotNull @org.hibernate.validator.constraints.UUID UUID messageId,

        @NotNull Long recipientId,

        @NotBlank String content) {
}
