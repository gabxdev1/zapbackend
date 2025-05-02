package br.com.gabxdev.repository;

import br.com.gabxdev.model.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, UUID> {

    @Query("""
            SELECT gm
            FROM GroupMessage gm
            WHERE gm.group.id IN (
                SELECT gmbr.group.id
                FROM GroupMember gmbr
                WHERE gmbr.user.id = :userId
            )
            """)
    List<GroupMessage> findAllGroupMessageForUser(@Param("userId") Long userId);

    @Query("""
            SELECT gm
            FROM GroupMessage gm
            WHERE gm.group.id IN (
                SELECT gmbr.group.id
                FROM GroupMember gmbr
                WHERE gmbr.user.id = :userId
            )
            AND gm.group.id = :groupId
            """)
    List<GroupMessage> findAllGroupMessageByGroupIdForUser(@Param("userId") Long userId, @Param("groupId") Long groupId);
}
