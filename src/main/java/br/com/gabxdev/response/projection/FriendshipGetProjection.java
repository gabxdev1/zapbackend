package br.com.gabxdev.response.projection;

import java.time.Instant;

public interface FriendshipGetProjection {

    UserGetProjection getUser1();

    UserGetProjection getUser2();

    Instant getCreatedAt();
}
