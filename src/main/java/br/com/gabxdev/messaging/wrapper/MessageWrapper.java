package br.com.gabxdev.messaging.wrapper;

import lombok.Builder;
import lombok.ToString;

import java.util.List;

@Builder
public record MessageWrapper<T>(
        T request,

        Long senderId,

        String senderEmail,

        List<String> roles
) {
}
