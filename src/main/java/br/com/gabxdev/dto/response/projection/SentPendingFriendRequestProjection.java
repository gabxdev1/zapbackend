package br.com.gabxdev.dto.response.projection;

import br.com.gabxdev.model.enums.RequestStatus;

public interface SentPendingFriendRequestProjection {
    UserPendingFriendRequestGetProjection getReceiver();

    RequestStatus getStatus();
}
