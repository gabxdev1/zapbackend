package br.com.gabxdev.repository;

import br.com.gabxdev.model.Group;
import br.com.gabxdev.model.GroupMember;
import br.com.gabxdev.model.User;
import br.com.gabxdev.model.pk.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
    boolean existsByIdAndIsModerator(GroupMemberId groupMemberId, boolean isModerator);

    boolean existsByIdAndCreatedBy(GroupMemberId groupMemberId, Long userId);

    void removeGroupMemberById(GroupMemberId id);
}
