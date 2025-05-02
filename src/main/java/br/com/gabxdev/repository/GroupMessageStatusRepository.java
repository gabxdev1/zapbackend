package br.com.gabxdev.repository;

import br.com.gabxdev.model.GroupMessage;
import br.com.gabxdev.model.GroupMessageStatus;
import br.com.gabxdev.model.enums.MessageStatus;
import br.com.gabxdev.model.pk.GroupMessageStatusId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GroupMessageStatusRepository extends JpaRepository<GroupMessageStatus, GroupMessageStatusId> {

    @Modifying
    @Query("""
                UPDATE GroupMessageStatus gms
                SET gms.status = 'READ', gms.readAt = CURRENT_TIMESTAMP
                WHERE gms.id.groupMessageId = :groupMessageId
                  AND gms.id.userId = :userId
                  AND gms.status <> 'READ'
            """)
    void markAsRead(@Param("userId") Long userId, @Param("groupMessageId") UUID groupMessageId);


    @Query("""
              SELECT gms
              FROM GroupMessageStatus gms
              JOIN gms.message msg
              JOIN msg.group g
              JOIN GroupMember gm ON gm.group.id = g.id
              WHERE gm.user.id = :userId
              AND (
                      msg.sender.id = :userId
                      OR gms.id.userId = :userId
                )
            """)
    List<GroupMessageStatus> findAllGroupMessageStatusForUser(@Param("userId") Long userId);

    @Query("""
              SELECT gms
              FROM GroupMessageStatus gms
              JOIN gms.message msg
              JOIN msg.group g
              JOIN GroupMember gm ON gm.group.id = g.id
              WHERE gm.user.id = :userId
              AND (
                      msg.sender.id = :userId
                      OR gms.id.userId = :userId
                )
              AND gms.message.group.id = :groupId
            """)
    List<GroupMessageStatus> findAllGroupMessageStatusByGroupIdForUser(@Param("userId") Long userId, @Param("groupId") Long groupId);


    int countFirstByMessage(GroupMessage groupMessage);

    int countReadByMessageAndStatus(GroupMessage message, MessageStatus status);
}
