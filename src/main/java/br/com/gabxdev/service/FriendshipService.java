package br.com.gabxdev.service;

import br.com.gabxdev.exception.NotFoundException;
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

    public Friendship findById(FriendshipId friendshipId) {
        return repository.findById(friendshipId)
                .orElseThrow(() -> new NotFoundException("Friendship not %s found".formatted(friendshipId)));
    }

    public Page<Friendship> findAllFriendShipsPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public boolean friendshipIsBlocked(FriendshipId friendshipId) {
        return repository.existsByIdAndBlockedIsTrue(friendshipId);
    }
}
