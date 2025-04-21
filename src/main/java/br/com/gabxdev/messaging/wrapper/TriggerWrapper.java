package br.com.gabxdev.messaging.wrapper;

import lombok.Builder;

import java.util.List;

@Builder
public record TriggerWrapper(
        Long senderId,

        String senderEmail,

        List<String> roles
) {
}
