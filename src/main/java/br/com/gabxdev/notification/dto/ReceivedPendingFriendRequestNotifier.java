package br.com.gabxdev.notification.dto;

import br.com.gabxdev.model.enums.RequestStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ReceivedPendingFriendRequestNotifier(
        RequestStatus status,

        UserPendingFriendRequestGetNotifier sender,

        Instant createdAt
) {
}
