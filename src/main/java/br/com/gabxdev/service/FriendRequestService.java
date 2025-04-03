package br.com.gabxdev.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    private final UserService userService;

    private final FriendshipService friendshipService;

    private final FriendshipRepository friendshipRepository;


    public FriendRequest findByIdOrElseThrowNotFound(FriendRequestId id) {
        return friendRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Friend request %s not found".formatted(id)));
    }

    public void sendFriendRequest(FriendRequestId friendRequestId) {
        assertNotSendingRequestToSelf(friendRequestId);

        var userSender = userService.findByIdOrThrowNotFound(friendRequestId.getSenderId());
        var userReceiver = userService.findByIdOrThrowNotFound(friendRequestId.getReceiverId());

        assertThatFriendshipIsNotBlocked(userSender.getId(), userReceiver.getId());

        var friendRequestIdToSave = FriendRequestId.builder()
                .senderId(userSender.getId())
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
        assertNotSendingRequestToSelf(friendRequestId);

        var userSender = userService.findByIdOrThrowNotFound(friendRequestId.getSenderId());
        var userReceiver = userService.findByIdOrThrowNotFound(friendRequestId.getReceiverId());

        assertThatExistFriendRequest(friendRequestId);
        assertThatFriendshipIsNotBlocked(friendRequestId.getSenderId(), friendRequestId.getReceiverId());

        var friendshipId = FriendshipId.builder()
                .userId1(userSender.getId())
                .userId2(userReceiver.getId())
                .build();

        var newFriendship = Friendship.builder()
                .id(friendshipId)
                .user1(userSender)
                .user2(userReceiver)
                .blocked(false)
                .build();

        var friendRequest = findByIdOrElseThrowNotFound(friendRequestId);

        friendRequest.setStatus(RequestStatus.ACCEPTED);

        friendshipRepository.save(newFriendship);
    }

    public void rejectFriendRequest(FriendRequestId friendRequestId) {
        assertNotSendingRequestToSelf(friendRequestId);

        var userSender = userService.findByIdOrThrowNotFound(friendRequestId.getSenderId());
        var userReceiver = userService.findByIdOrThrowNotFound(friendRequestId.getReceiverId());

        assertThatExistFriendRequest(friendRequestId);
        assertRequestNotAlreadyAccepted(friendRequestId);
        assertThatFriendshipIsNotBlocked(friendRequestId.getSenderId(), friendRequestId.getReceiverId());

        friendRequestRepository.deleteById(friendRequestId);
    }

    private void assertThatFriendshipIsNotBlocked(Long sender, Long receiver) {
        var friendshipId = FriendshipId.builder()
                .userId1(sender)
                .userId2(receiver)
                .build();

        if (friendshipService.friendshipIsBlocked(friendshipId)) {
            throw new UserBlockedException("Friendship blocked");
        }
    }

    private void assertThatExistFriendRequest(FriendRequestId friendRequestId) {
        if (!friendRequestRepository.existsById(friendRequestId)) {
            throw new NotFoundException("Friend request %s not found".formatted(friendRequestId));
        }
    }

    private void assertNotSendingRequestToSelf(FriendRequestId friendRequestId) {
        if (friendRequestId.getSenderId().equals(friendRequestId.getReceiverId())) {
            throw new ForbiddenException("You are not allowed to send request to self");
        }
    }

    private void assertRequestNotAlreadyAccepted(FriendRequestId friendRequestId) {
        if (friendRequestRepository.existsByIdAndStatusNot(friendRequestId, RequestStatus.ACCEPTED)) {
            throw new ForbiddenException("It is not possible to reject a friend request that has already been accepted.");
        }
    }

}
