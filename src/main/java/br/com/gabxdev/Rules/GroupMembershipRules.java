package br.com.gabxdev.Rules;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.model.Group;
import br.com.gabxdev.model.pk.GroupMemberId;
import br.com.gabxdev.repository.GroupMemberRepository;
import br.com.gabxdev.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMembershipRules {

    private final GroupMemberRepository groupMemberRepository;

    private final AuthUtil auth;
    private final GroupRepository groupRepository;

    public void assertCurrentUserIsGroupModerator(Group group) {
        var currentUserId = auth.getCurrentUser();

        var groupMemberId = GroupMemberId.builder().userId(currentUserId.getId()).groupId(group.getId()).build();

        if (groupMemberRepository.existsByIdAndModerator(groupMemberId, true)) {
            throw new ForbiddenException("Only group admin can perform this action");
        }
    }
}
