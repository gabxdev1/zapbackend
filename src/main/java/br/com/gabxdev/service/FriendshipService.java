package br.com.gabxdev.service;

import br.com.gabxdev.model.Friendship;
import br.com.gabxdev.model.pk.FriendshipId;
import br.com.gabxdev.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository repository;

    public void blockFriend(Long friendId) {

    }

    public void unblockFriend(Long friendId) {

    }

    public void removeFriend(Long friendId) {

    }

    public Page<Friendship> findAllFriendShips(Long userId, Pageable pageable) {
        return null;
    }

    public boolean friendshipIsBlocked(FriendshipId friendshipId) {
        return repository.existsByIdAndBlockedIsTrue(friendshipId);
    }
}
