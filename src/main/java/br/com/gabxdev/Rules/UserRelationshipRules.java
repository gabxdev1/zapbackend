package br.com.gabxdev.Rules;

import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.model.pk.FriendshipId;
import br.com.gabxdev.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRelationshipRules {

    private final FriendshipRepository friendshipRepository;

    public void assertNotSendingRequestToSelf(Long toId, Long fromId) {
        if (toId.equals(fromId)) {
            throw new ForbiddenException("You are not allowed to send request to self");
        }
    }

    public boolean friendshipIsBlocked(FriendshipId friendshipId) {
        return friendshipRepository.existsByIdAndBlockedIsTrue(friendshipId);
    }
}
