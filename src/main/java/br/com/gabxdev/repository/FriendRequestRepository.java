package br.com.gabxdev.repository;

import br.com.gabxdev.model.FriendRequest;
import br.com.gabxdev.model.enums.RequestStatus;
import br.com.gabxdev.model.pk.FriendRequestId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, FriendRequestId> {
    boolean existsByIdAndStatusEquals(FriendRequestId id, RequestStatus status);

    boolean existsByIdAndStatusNot(FriendRequestId id, RequestStatus accepted);
}
