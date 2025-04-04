package br.com.gabxdev.service;

import br.com.gabxdev.Rules.UserRelationshipRules;
import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.UserBlock;
import br.com.gabxdev.model.pk.FriendshipId;
import br.com.gabxdev.model.pk.UserBlockId;
import br.com.gabxdev.repository.UserBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBlockService {

    private final UserBlockRepository userBlockRepository;

    private final FriendshipService friendshipService;

    private final UserRelationshipRules rules;

    private final UserService userService;

    private final AuthUtil auth;

    public Optional<UserBlock> findById(UserBlockId id) {
        return userBlockRepository.findById(id);
    }

    public UserBlock findByIdOrElseThrowNotFound(UserBlockId id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("You are trying to unblock a user who is not blocked"));
    }

    @Transactional
    public void blockUser(Long idUser) {
        var userBlocker = auth.getCurrentUser();
        var userToBlock = userService.findByIdOrThrowNotFound(idUser);

        rules.assertNotSendingRequestToSelf(userBlocker.getId(), userToBlock.getId());

        var blockId = UserBlockId.builder()
                .blocker(userBlocker.getId())
                .blocked(userToBlock.getId())
                .build();

        var block = UserBlock.builder()
                .id(blockId)
                .blocker(userBlocker)
                .blocked(userToBlock)
                .isBlocked(true)
                .build();

        var friendshipId = FriendshipId.builder().userId1(userBlocker.getId()).userId2(userToBlock.getId()).build();

        friendshipService.removeFriendshipIfExists(friendshipId);

        userBlockRepository.save(block);
    }

    public void unblockUser(Long idUser) {
        var userBlocker = auth.getCurrentUser();

        rules.assertNotSendingRequestToSelf(userBlocker.getId(), idUser);

        var blockId = UserBlockId.builder().blocker(userBlocker.getId()).blocked(idUser).build();

        userBlockRepository.deleteById(blockId);
    }
}
