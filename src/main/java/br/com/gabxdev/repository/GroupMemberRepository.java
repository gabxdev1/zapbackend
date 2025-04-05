package br.com.gabxdev.repository;

import br.com.gabxdev.model.GroupMember;
import br.com.gabxdev.model.pk.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
    boolean existsByIdAndModerator(GroupMemberId groupMemberId, boolean isModerator);

    boolean existsByIdAndCreatedBy(GroupMemberId groupMemberId, Long userId);

    void removeGroupMemberById(GroupMemberId id);
}
