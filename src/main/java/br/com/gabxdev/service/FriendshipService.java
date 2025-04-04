package br.com.gabxdev.service;

import br.com.gabxdev.Rules.UserRelationshipRules;
import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.Friendship;
import br.com.gabxdev.model.pk.FriendshipId;
import br.com.gabxdev.repository.FriendshipRepository;
import br.com.gabxdev.response.FriendshipGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository repository;

    private final UserRelationshipRules rules;

    private final AuthUtil authUtil;

    public Page<FriendshipGetResponse> findAllFriendshipsPaginated(Pageable pageable) {
        var currentUserId = authUtil.getCurrentUser().getId();
        return repository.findAllFriendshipByUserIdPaginated(currentUserId, pageable);
    }

    public Optional<Friendship> findById(FriendshipId id) {
        var idUserId1 = id.getUserId1();
        var idUserId2 = id.getUserId2();

        return repository.findFriendshipById(idUserId1, idUserId2);
    }

    public Friendship findByIdOrElseThrowNotFound(FriendshipId id) {
        rules.assertNotSendingRequestToSelf(id.getUserId1(), id.getUserId2());

        return findById(id)
                .orElseThrow(() -> new NotFoundException("Friendship not %s found".formatted(id)));
    }

    public void removeFriendshipIfExists(FriendshipId id) {
        rules.assertNotSendingRequestToSelf(id.getUserId1(), id.getUserId2());

        findById(id).ifPresent(repository::delete);
    }
}
