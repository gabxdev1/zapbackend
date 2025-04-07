package br.com.gabxdev.messaging.wrapper;

import br.com.gabxdev.dto.request.privateMessage.PrivateMessageSendRequest;
import lombok.Builder;

import java.util.List;

@Builder
public record PrivateMessageWrapper(
        PrivateMessageSendRequest request,

        Long senderId,

        String senderEmail,

        List<String> roles
) {
}
