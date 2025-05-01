package br.com.gabxdev.dto.request.group_message;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record GroupMessageSendRequest(
        @NotNull
        @org.hibernate.validator.constraints.UUID
        UUID messageId,

        @NotNull
        @Min(1)
        Long groupId,

        @NotBlank
        String content
) {


}
