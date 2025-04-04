package br.com.gabxdev.response;

import java.time.Instant;

public record FriendshipGetResponse(
        Long id,

        String firstName,

        String lastName,

        String email,

        Instant lastSeen,

        Instant createdAt
) {
}