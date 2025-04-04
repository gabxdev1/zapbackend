package br.com.gabxdev.Rules;

import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.model.pk.FriendshipId;
import br.com.gabxdev.repository.UserBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRelationshipRules {

    private final UserBlockRepository userBlockRepository;

    public void assertNotSendingRequestToSelf(Long toId, Long fromId) {
        if (toId.equals(fromId)) {
            throw new ForbiddenException("You are not allowed to send request to self");
        }
    }

    public boolean friendIsBlocked(FriendshipId friendshipId) {
        var idUserId1 = friendshipId.getUserId1();
        var idUserId2 = friendshipId.getUserId2();

        return userBlockRepository.isBlocked(idUserId1, idUserId2);
    }
}
