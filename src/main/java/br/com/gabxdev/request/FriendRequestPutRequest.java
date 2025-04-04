package br.com.gabxdev.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record FriendRequestPutRequest(
        @NotNull
        @Min(1)
        Long senderId,

        @JsonIgnore
        Long receiverId
) {
}
