package br.com.gabxdev.request;

import jakarta.validation.constraints.Min;

public record FriendshipDeleteRequest(@Min(1) Long userId) {
}
