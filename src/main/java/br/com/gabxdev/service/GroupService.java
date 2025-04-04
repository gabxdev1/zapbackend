package br.com.gabxdev.service;

import br.com.gabxdev.Rules.GroupMembershipRules;
import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.Group;
import br.com.gabxdev.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;

    private final AuthUtil auth;

    private final UserService userService;

    private final GroupMembershipRules rules;

    public Optional<Group> findById(Long groupId) {
        return repository.findById(groupId);
    }

    public Group findByIdOrThrowNotFound(Long groupId) {
        return findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group %d not found".formatted(groupId)));
    }

    public Group createGroup(String name, String description, Set<Long> memberIds) {
        var currentUser = auth.getCurrentUser();

        var newGroup = Group.builder().name(name).description(description).build();

        newGroup.addMember(currentUser, true);

        memberIds.forEach(memberId -> {
            var newMember = userService.findByIdOrThrowNotFound(memberId);
            newGroup.addMember(newMember, false);
        });

        return repository.save(newGroup);
    }

    public Group updateGroup(Long groupId, String name, String description) {
        var groupToUpdate = findByIdOrThrowNotFound(groupId);

        rules.assertCurrentUserIsGroupModerator(groupToUpdate);

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
