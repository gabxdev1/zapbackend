package br.com.gabxdev.service;

import br.com.gabxdev.Rules.GroupMembershipRules;
import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.pk.GroupMemberId;
import br.com.gabxdev.repository.GroupMemberRepository;
import br.com.gabxdev.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    private final GroupService groupService;

    private final UserService userService;

    private final GroupMembershipRules rules;

    private final GroupRepository groupRepository;

    private final AuthUtil auth;

    public void addMembers(Long groupId, Set<Long> userIds) {
        var group = groupService.findByIdOrThrowNotFound(groupId);

        rules.assertCurrentUserIsGroupModerator(group);

        userIds.forEach(memberId -> {
            var newMember = userService.findByIdOrThrowNotFound(memberId);

            assertUserNotMemberOfGroup(groupId, newMember.getId());

            group.addMember(newMember, false);
        });

        groupRepository.save(group);
    }

    public void removeMember(Long groupId, Long userId) {
        var group = groupService.findByIdOrThrowNotFound(groupId);

        var userToRemove = userService.findByIdOrThrowNotFound(userId);

        rules.assertCurrentUserIsGroupModerator(group);

        assertUserIsMemberOfGroup(groupId, userToRemove.getId());

        assertUserIsNotGroupCreator(groupId, userToRemove.getId());

        var groupMemberId = getGroupMemberId(groupId, userId);

        groupMemberRepository.removeGroupMemberById(groupMemberId);
    }

    private void assertUserIsMemberOfGroup(Long groupId, Long userId) {
        var groupMemberId = getGroupMemberId(groupId, userId);

        if (!isMemberOfGroup(groupMemberId)) {
            throw new NotFoundException("User is not a member of this group.");
        }
    }

    private void assertUserNotMemberOfGroup(Long groupId, Long userId) {
        var groupMemberId = getGroupMemberId(groupId, userId);

        if (isMemberOfGroup(groupMemberId)) {
            throw new ForbiddenException("User is already a member of the group");
        }
    }

    private void assertUserIsNotGroupCreator(Long groupId, Long userId) {
        var groupMemberId = getGroupMemberId(groupId, userId);

        if (groupMemberRepository.existsByIdAndCreatedBy(groupMemberId, userId)) {
            throw new ForbiddenException("Group creator cannot be removed from the group");
        }
    }

    private boolean isMemberOfGroup(GroupMemberId groupMemberId) {
        return groupMemberRepository.existsById(groupMemberId);
    }

    private GroupMemberId getGroupMemberId(Long groupId, Long userId) {
        var currentUserId = auth.getCurrentUser().getId();

        return GroupMemberId.builder().userId(currentUserId).groupId(groupId).build();
    }
}