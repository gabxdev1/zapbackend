package br.com.gabxdev.dto.request.friend_request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record FriendRequestPutRequest(@NotNull @Min(1) Long senderId) {
}
