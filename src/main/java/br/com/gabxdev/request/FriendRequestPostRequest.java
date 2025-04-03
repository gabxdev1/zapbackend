package br.com.gabxdev.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record FriendRequestPostRequest(
        @NotNull
        @Min(1)
        Long senderId,

        @NotNull
        @Min(1)
        Long receiverId
) {
}
