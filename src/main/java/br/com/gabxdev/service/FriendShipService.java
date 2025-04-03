package br.com.gabxdev.service;

import br.com.gabxdev.model.FriendShip;
import br.com.gabxdev.repository.FriendShipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendShipService {

    private final FriendShipRepository repository;

    public void sendFriendRequest(Long senderId, Long receiverId) {

    }

    public void acceptFriendRequest(Long requestId) {

    }

    public void rejectFriendRequest(Long requestId) {

    }

    public void blockFriend(Long friendId) {

    }

    public void unblockFriend(Long friendId) {

    }

    public void removeFriend(Long friendId) {

    }

    public Page<FriendShip> findAllFriendShips(Long userId, Pageable pageable) {
        return null;
    }
}
