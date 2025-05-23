package br.com.gabxdev.dto.request.friend_request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record FriendRequestPostRequest(
        @NotNull
        @Min(1)
        Long receiverId
) {
}
