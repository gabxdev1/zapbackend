package br.com.gabxdev.notification.dto;

import lombok.Builder;

@Builder
public record UserPendingFriendRequestGetNotifier(
        Long id,

        String firstName,

        String lastName,

        String nickname
) {

}
