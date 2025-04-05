package br.com.gabxdev.service;

import br.com.gabxdev.Rules.UserRelationshipRules;
import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.exception.UserBlockedException;
import br.com.gabxdev.model.FriendRequest;
import br.com.gabxdev.model.Friendship;
import br.com.gabxdev.model.enums.RequestStatus;
import br.com.gabxdev.model.pk.FriendRequestId;
import br.com.gabxdev.model.pk.FriendshipId;
import br.com.gabxdev.repository.FriendRequestRepository;
import br.com.gabxdev.repository.FriendshipRepository;
import br.com.gabxdev.response.projection.ReceivedPendingFriendRequestProjection;
import br.com.gabxdev.response.projection.SentPendingFriendRequestProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    private final UserService userService;

    private final FriendshipRepository friendshipRepository;

    private final AuthUtil authUtil;

    private final UserRelationshipRules rules;

    public FriendRequest findByIdOrElseThrowNotFound(FriendRequestId id) {
        return friendRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Friend request %s not found".formatted(id)));
    }

    public Page<SentPendingFriendRequestProjection> getPendingSentRequests(Pageable pageable) {
        var currentUser = authUtil.getCurrentUser();

        return friendRequestRepository.findAllById_SenderIdAndStatusNot(currentUser.getId(), RequestStatus.ACCEPTED, pageable);
    }

    public Page<ReceivedPendingFriendRequestProjection> getPendingReceivedRequests(Pageable pageable) {
        var currentUser = authUtil.getCurrentUser();

        return friendRequestRepository.findAllById_ReceiverIdAndStatusNot(currentUser.getId(), RequestStatus.ACCEPTED, pageable);
    }

    public void sendFriendRequest(FriendRequestId friendRequestId) {
        rules.assertUserIsNotSelf(friendRequestId.getSenderId(), friendRequestId.getReceiverId());

        var userSender = userService.findByIdOrThrowNotFound(friendRequestId.getSenderId());
        var userReceiver = userService.findByIdOrThrowNotFound(friendRequestId.getReceiverId());

        assertNoDuplicateRequest(friendRequestId);
        assertThatFriendshipIsNotBlocked(userSender.getId(), userReceiver.getId());

        var friendRequestIdToSave = FriendRequestId.builder()
                .senderId(friendRequestId.getSenderId())
                .receiverId(userReceiver.getId())
                .build();

        var friendRequest = FriendRequest.builder()
                .id(friendRequestIdToSave)
                .sender(userSender)
                .receiver(userReceiver)
                .status(RequestStatus.PENDING)
                .build();

        friendRequestRepository.save(friendRequest);
    }

    @Transactional
    public void acceptFriendRequest(FriendRequestId friendRequestId) {
        rules.assertUserIsNotSelf(friendRequestId.getSenderId(), friendRequestId.getReceiverId());

        var userSender = userService.findByIdOrThrowNotFound(friendRequestId.getSenderId());
        var userReceiver = userService.findByIdOrThrowNotFound(friendRequestId.getReceiverId());

        assertThatExistFriendRequestAndStatusIsPending(friendRequestId);
        assertThatFriendshipIsNotBlocked(friendRequestId.getSenderId(), friendRequestId.getReceiverId());

        var friendshipId = FriendshipId.builder()
                .userId1(userSender.getId())
                .userId2(userReceiver.getId())
                .build();

        var newFriendship = Friendship.builder()
                .id(friendshipId)
                .user1(userSender)
                .user2(userReceiver)
                .build();

        var friendRequest = findByIdOrElseThrowNotFound(friendRequestId);

        friendRequest.setStatus(RequestStatus.ACCEPTED);

        friendshipRepository.save(newFriendship);
    }

    public void rejectFriendRequest(FriendRequestId friendRequestId) {
        rules.assertUserIsNotSelf(friendRequestId.getSenderId(), friendRequestId.getReceiverId());

        assertThatExistFriendRequestAndStatusIsPending(friendRequestId);

        friendRequestRepository.deleteById(friendRequestId);
    }

    private void assertThatFriendshipIsNotBlocked(Long sender, Long receiver) {
        var friendshipId = FriendshipId.builder()
                .userId1(sender)
                .userId2(receiver)
                .build();

        if (rules.friendIsBlocked(friendshipId)) {
            throw new UserBlockedException("Friendship blocked");
        }
    }

    private void assertThatExistFriendRequestAndStatusIsPending(FriendRequestId friendRequestId) {
        if (!existsFriendRequest(friendRequestId, RequestStatus.PENDING)) {
            throw new NotFoundException("Friend request %s not found or already accepted".formatted(friendRequestId));
        }
    }

    private void assertNoDuplicateRequest(FriendRequestId friendRequestId) {
        if (existsFriendRequest(friendRequestId, RequestStatus.PENDING)) {
            throw new ForbiddenException("A pending friend request already exists between these users.");
        }
    }

    private boolean existsFriendRequest(FriendRequestId friendRequestId, RequestStatus status) {
        var senderId = friendRequestId.getSenderId();
        var receiverId = friendRequestId.getReceiverId();

        return friendRequestRepository.existsBySenderIdAndReceiverIdAndStatus(senderId, receiverId, status) ||
               friendRequestRepository.existsBySenderIdAndReceiverIdAndStatus(receiverId, senderId, status);
    }
}