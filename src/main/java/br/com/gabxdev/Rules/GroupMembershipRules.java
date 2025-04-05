package br.com.gabxdev.Rules;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.Group;
import br.com.gabxdev.model.pk.GroupMemberId;
import br.com.gabxdev.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMembershipRules {

    private final GroupMemberRepository groupMemberRepository;

    private final AuthUtil auth;

    public boolean isMemberOfGroup(GroupMemberId groupMemberId) {
        return groupMemberRepository.existsById(groupMemberId);
    }

    public void assertCurrentUserIsGroupModerator(Group group) {
        var currentUserId = auth.getCurrentUser();

        var groupMemberId = GroupMemberId.builder().userId(currentUserId.getId()).groupId(group.getId()).build();

        if (!groupMemberRepository.existsByIdAndModerator(groupMemberId, true)) {
            throw new ForbiddenException("Only group admin can perform this action");
        }
    }

    public void assertUserIsMemberOfGroupThrowNotFound(Long groupId, Long userId) {
        var groupMemberId = createGroupMemberId(groupId, userId);

        if (!isMemberOfGroup(groupMemberId)) {
            throw new NotFoundException("User is not a member of this group.");
        }
    }

    public void assertUserIsMemberOfGroupThrowForbidden(Long groupId, Long userId) {
        var groupMemberId = createGroupMemberId(groupId, userId);

        if (!isMemberOfGroup(groupMemberId)) {
            throw new ForbiddenException("User is not a member of this group.");
        }
    }

    public GroupMemberId createGroupMemberId(Long groupId, Long userId) {
        return GroupMemberId.builder().userId(userId).groupId(groupId).build();
    }
}
