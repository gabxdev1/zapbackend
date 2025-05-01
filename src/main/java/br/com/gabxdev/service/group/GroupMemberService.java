package br.com.gabxdev.service.group;

import br.com.gabxdev.Rules.GroupMembershipRules;
import br.com.gabxdev.Rules.UserRelationshipRules;
import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.messaging.producer.Producer;
import br.com.gabxdev.model.Group;
import br.com.gabxdev.model.GroupMember;
import br.com.gabxdev.model.pk.GroupMemberId;
import br.com.gabxdev.notification.dto.GroupNewMemberNotification;
import br.com.gabxdev.repository.GroupMemberRepository;
import br.com.gabxdev.repository.GroupRepository;
import br.com.gabxdev.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static br.com.gabxdev.config.RabbitMQConfig.Exchanges.TOPIC_GROUP_EVENTS;
import static br.com.gabxdev.config.RabbitMQConfig.RoutingKeys.GROUP_NEW_MEMBER_NOTIFICATION;

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

    private final Producer producer;

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

        var groupNewMemberNotification = GroupNewMemberNotification
                .builder().groupId(groupId).build();

        producer.sendMessage(groupNewMemberNotification, TOPIC_GROUP_EVENTS, GROUP_NEW_MEMBER_NOTIFICATION);
    }

    public void removeMember(Long groupId, Long userToRemoveId) {
        var currentUserId = auth.getCurrentUser().getId();

        userRelationshipRules.assertUserIsNotSelf(currentUserId, userToRemoveId);

        var group = groupService.findByIdOrThrowNotFound(groupId);

        groupMembershipRules.assertCurrentUserIsGroupModerator(group);

        validateMemberRemoval(groupId, userToRemoveId);

        var groupMemberId = groupMembershipRules.createGroupMemberId(groupId, userToRemoveId);

        groupMemberRepository.deleteById(groupMemberId);
    }

    public void promoteToModerator(Long groupId, Long userToPromoteId) {
        var currentUserId = auth.getCurrentUser().getId();

        var group = groupService.findByIdOrThrowNotFound(groupId);

        userService.findByIdOrThrowNotFound(userToPromoteId);

        userRelationshipRules.assertUserIsNotSelf(currentUserId, userToPromoteId);

        groupMembershipRules.assertCurrentUserIsGroupModerator(group);

        var groupMemberId = groupMembershipRules.createGroupMemberId(groupId, userToPromoteId);

        var groupMember = findByIdOrThrowNotFound(groupMemberId);

        groupMember.setModerator(true);

        groupMemberRepository.save(groupMember);
    }

    public void demoteFromModerator(Long groupId, Long userToDemoteId) {
        var currentUserId = auth.getCurrentUser().getId();

        var group = groupService.findByIdOrThrowNotFound(groupId);

        userService.findByIdOrThrowNotFound(userToDemoteId);

        userRelationshipRules.assertUserIsNotSelf(currentUserId, userToDemoteId);

        assertUserIsNotGroupCreator(groupId, userToDemoteId);

        groupMembershipRules.assertCurrentUserIsGroupModerator(group);

        var groupMemberId = groupMembershipRules.createGroupMemberId(groupId, userToDemoteId);
        var groupMember = findByIdOrThrowNotFound(groupMemberId);

        groupMember.setModerator(false);

        groupMemberRepository.save(groupMember);
    }

    private void validateMemberRemoval(Long groupId, Long userToRemoveId) {
        userService.findByIdOrThrowNotFound(userToRemoveId);

        groupMembershipRules.assertUserIsMemberOfGroupThrowNotFound(groupId, userToRemoveId);

        assertUserIsNotGroupCreator(groupId, userToRemoveId);
    }

    private void assertUserNotMemberOfGroup(Long groupId, Long userId) {
        var groupMemberId = groupMembershipRules.createGroupMemberId(groupId, userId);

        if (groupMembershipRules.isMemberOfGroup(groupMemberId)) {
            throw new ForbiddenException("User is already a member of the group");
        }
    }

    private void assertUserIsNotGroupCreator(Long groupId, Long userId) {
        var groupMemberId = groupMembershipRules.createGroupMemberId(groupId, userId);

        if (groupMemberRepository.existsByIdAndCreatedBy(groupMemberId, userId)) {
            throw new ForbiddenException("You do not have permission to perform this action on the group creator");
        }
    }

    public int countMembersByGroup(Group group) {
        return groupMemberRepository.countGroupMemberByGroup(group);
    }
}