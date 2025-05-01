package br.com.gabxdev.repository;

import br.com.gabxdev.dto.response.projection.SentPendingFriendRequestProjection;
import br.com.gabxdev.model.FriendRequest;
import br.com.gabxdev.model.enums.RequestStatus;
import br.com.gabxdev.model.pk.FriendRequestId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, FriendRequestId> {

    Page<SentPendingFriendRequestProjection> findAllById_SenderIdAndStatusNot(Long idSenderId, RequestStatus status, Pageable pageable);

    List<FriendRequest> findAllById_ReceiverIdAndStatusNot(Long idReceiverId, RequestStatus status);

    boolean existsBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, RequestStatus status);
}
