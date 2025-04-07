package br.com.gabxdev.repository;

import br.com.gabxdev.model.GroupMember;
import br.com.gabxdev.model.pk.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
    boolean existsByIdAndModerator(GroupMemberId groupMemberId, boolean isModerator);

    boolean existsByIdAndCreatedBy(GroupMemberId groupMemberId, Long userId);

    void removeGroupMemberById(GroupMemberId id);

    @Query("""
            SELECT DISTINCT u.email FROM GroupMember gm
            JOIN gm.user u
            WHERE gm.group.id IN (
                SELECT gm2.group.id FROM GroupMember gm2
                WHERE gm2.user.id = :currentUserId
            ) AND gm.user.id <> :currentUserId
            """)
    List<String> findMemberEmailsByUserId(Long currentUserId);
}
