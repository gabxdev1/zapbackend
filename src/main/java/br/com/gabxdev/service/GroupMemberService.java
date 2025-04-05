package br.com.gabxdev.service;

import br.com.gabxdev.Rules.GroupMembershipRules;
import br.com.gabxdev.Rules.UserRelationshipRules;
import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.GroupMember;
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

    private final GroupMembershipRules groupMembershipRules;

    private final GroupRepository groupRepository;

    private final UserRelationshipRules userRelationshipRules;

    private final AuthUtil auth;

    public GroupMember findByIdOrThrowNotFound(GroupMemberId id) {
        return groupMemberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Group member %s not found".formatted(id)));
    }

    public void addMembers(Long groupId, Set<Long> userIds) {
        var currentUserId = auth.getCurrentUser().getId();

        userRelationshipRules.assertUserIsNotSelf(currentUserId, userIds);

        var group = groupService.findByIdOrThrowNotFound(groupId);

        groupMembershipRules.assertCurrentUserIsGroupModerator(group);

        userIds.forEach(memberId -> {
            var newMember = userService.findByIdOrThrowNotFound(memberId);

            assertUserNotMemberOfGroup(groupId, newMember.getId());

            group.addMember(newMember, false);
        });

        groupRepository.save(group);
    }

    public void removeMember(Long groupId, Long userToRemoveId) {
        var currentUserId = auth.getCurrentUser().getId();

        userRelationshipRules.assertUserIsNotSelf(currentUserId, userToRemoveId);

        var group = groupService.findByIdOrThrowNotFound(groupId);

        groupMembershipRules.assertCurrentUserIsGroupModerator(group);

        validateMemberRemoval(groupId, userToRemoveId);

        var groupMemberId = getGroupMemberId(groupId, userToRemoveId);

        groupMemberRepository.removeGroupMemberById(groupMemberId);
    }

    public void promoteToAdmin(Long groupId, Long userToPromoteId) {
        var group = groupService.findByIdOrThrowNotFound(groupId);

        var userToPromote = userService.findByIdOrThrowNotFound(userToPromoteId);

        groupMembershipRules.assertCurrentUserIsGroupModerator(group);

        var groupMemberId = getGroupMemberId(groupId, userToPromote.getId());

        var groupMember = findByIdOrThrowNotFound(groupMemberId);

        groupMember.setModerator(true);

        groupMemberRepository.save(groupMember);
    }

    private void validateMemberRemoval(Long groupId, Long userToRemoveId) {
        userService.findByIdOrThrowNotFound(userToRemoveId);

        assertUserIsMemberOfGroup(groupId, userToRemoveId);

        assertUserIsNotGroupCreator(groupId, userToRemoveId);
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
        return GroupMemberId.builder().userId(userId).groupId(groupId).build();
    }
}