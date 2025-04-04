package br.com.gabxdev.response.projection;

import java.time.Instant;

public interface UserGetProjection {
    Long getId();

    String getFirstName();

    String getLastName();

    String getEmail();

    Instant getLastSeen();
}
