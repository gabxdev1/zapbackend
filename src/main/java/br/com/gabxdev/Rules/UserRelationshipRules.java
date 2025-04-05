package br.com.gabxdev.Rules;

import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.model.pk.FriendshipId;
import br.com.gabxdev.repository.UserBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class UserRelationshipRules {

    private final UserBlockRepository userBlockRepository;

    public void assertUserIsNotSelf(Long toId, Long fromId) {
        if (toId.equals(fromId)) {
            throw new ForbiddenException("You are not allowed to send request to self");
        }
    }

    public void assertUserIsNotSelf(Long toId, Collection<Long> usersId) {
        usersId.forEach(userId -> {
            if (toId.equals(userId)) {
                throw new ForbiddenException("You are not allowed to send request to self");
            }
        });
    }

    public boolean friendIsBlocked(FriendshipId friendshipId) {
        var idUserId1 = friendshipId.getUserId1();
        var idUserId2 = friendshipId.getUserId2();

        return userBlockRepository.isBlocked(idUserId1, idUserId2)
                .orElse(false);
    }
}
