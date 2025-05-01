package br.com.gabxdev.service.group;

import br.com.gabxdev.Rules.GroupMembershipRules;
import br.com.gabxdev.Rules.UserRelationshipRules;
import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.messaging.producer.Producer;
import br.com.gabxdev.model.Group;
import br.com.gabxdev.notification.dto.GroupCreatedNotification;
import br.com.gabxdev.repository.GroupRepository;
import br.com.gabxdev.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static br.com.gabxdev.config.RabbitMQConfig.Exchanges.TOPIC_GROUP_EVENTS;
import static br.com.gabxdev.config.RabbitMQConfig.RoutingKeys.GROUP_CREATED_NOTIFICATION;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;

    private final AuthUtil auth;

    private final UserService userService;

    private final GroupMembershipRules groupMembershipRules;

    private final UserRelationshipRules userRelationshipRules;

    private final Producer producer;

    private Optional<Group> findById(Long groupId) {
        return repository.findById(groupId);
    }

    public Group findGroupFullDetailsByIdOrThrow(Long groupId) {
        return repository.findGroupByIdFullDetails(groupId)
                .orElseThrow(() -> new NotFoundException("Group %d not found".formatted(groupId)));
    }

    public Group findByIdOrThrowNotFound(Long groupId) {
        return findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group %d not found".formatted(groupId)));
    }

    public List<Group> getGroupFullDetails() {
        var currentUserId = auth.getCurrentUser().getId();

        return repository.findAllGroupsFullDetailsForUser(currentUserId);
    }

    public Group createGroup(String name, String description, Set<Long> memberIds) {
        var currentUserId = auth.getCurrentUser().getId();

        userRelationshipRules.assertUserIsNotSelf(currentUserId, memberIds);

        var newGroup = Group.builder().name(name).description(description).build();

        var userModerator = userService.findByIdOrThrowNotFound(currentUserId);

        newGroup.addMember(userModerator, true);

        memberIds.forEach(memberId -> {
            var newMember = userService.findByIdOrThrowNotFound(memberId);
            newGroup.addMember(newMember, false);
        });

        var newGroupSaved = repository.save(newGroup);

        var groupCreatedNotifier = GroupCreatedNotification
                .builder().groupId(newGroupSaved.getId()).membersId(memberIds).build();

        producer.sendMessage(groupCreatedNotifier, TOPIC_GROUP_EVENTS, GROUP_CREATED_NOTIFICATION);

        return newGroupSaved;
    }

    public Group updateGroup(Long groupId, String name, String description) {
        var groupToUpdate = findByIdOrThrowNotFound(groupId);

        groupMembershipRules.assertCurrentUserIsGroupModerator(groupToUpdate);

        groupToUpdate.setName(name);
        groupToUpdate.setDescription(description);

        return repository.save(groupToUpdate);
    }

    public void deleteGroup(Long groupId) {
        var groupToDelete = findByIdOrThrowNotFound(groupId);

        assertCurrentUserIsGroupCreator(groupToDelete);

        repository.delete(groupToDelete);
    }

    private void assertCurrentUserIsGroupCreator(Group group) {
        var idCurrentUser = auth.getCurrentUser().getId();

        if (!(group.getCreatedBy().equals(idCurrentUser) || group.getMembers().size() == 1)) {
            throw new ForbiddenException("Only the group creator can delete the group");
        }
    }

}
