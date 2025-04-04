package br.com.gabxdev.service;

import br.com.gabxdev.Rules.UserRelationshipRules;
import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.Friendship;
import br.com.gabxdev.model.pk.FriendRequestId;
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

    private final UserRelationshipRules rules;

    public Friendship findByIdOrElseThrowNotFound(FriendshipId friendshipId) {
        rules.assertNotSendingRequestToSelf(friendshipId.getUserId1(), friendshipId.getUserId2());

        return repository.findById(friendshipId)
                .orElseThrow(() -> new NotFoundException("Friendship not %s found".formatted(friendshipId)));
    }

    public Page<Friendship> findAllFriendShipsPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void blockFriendship(FriendshipId friendshipId) {
        rules.assertNotSendingRequestToSelf(friendshipId.getUserId1(), friendshipId.getUserId2());

        var friendship = findByIdOrElseThrowNotFound(friendshipId);

        friendship.setBlocked(true);

        repository.save(friendship);
    }

    public void unblockFriendship(FriendshipId friendshipId) {
        rules.assertNotSendingRequestToSelf(friendshipId.getUserId1(), friendshipId.getUserId2());

        var friendship = findByIdOrElseThrowNotFound(friendshipId);

        friendship.setBlocked(false);

        repository.save(friendship);
    }

    public void removeFriendship(FriendshipId friendshipId) {
        rules.assertNotSendingRequestToSelf(friendshipId.getUserId1(), friendshipId.getUserId2());

        var friendship = findByIdOrElseThrowNotFound(friendshipId);

        repository.delete(friendship);
    }
}
