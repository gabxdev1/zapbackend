package br.com.gabxdev.dto.response.projection;

import java.time.Instant;

public interface UserPendingFriendRequestGetProjection {

    Long getId();

    String getFirstName();

    String getLastName();

    String getEmail();

    Instant getLastSeen();

    Instant getCreatedAt();
}
