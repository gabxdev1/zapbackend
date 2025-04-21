package br.com.gabxdev.repository;

import br.com.gabxdev.model.PrivateMessage;
import br.com.gabxdev.model.enums.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, UUID> {

    @Query(
            """
                        SELECT pv FROM PrivateMessage pv
                        WHERE ((pv.recipient.id = :currentUserId) AND (pv.message.status = "RECEIVED" OR pv.message.status = "READ")) OR
                              ((pv.sender.id = :currentUserId) AND (pv.message.status = "SENT" OR pv.message.status = "RECEIVED" OR pv.message.status = "READ"))
                        ORDER BY pv.createdAt DESC
                    """)
    List<PrivateMessage> findAllPrivateMessage(Long currentUserId);

    @Query(
            """
                    SELECT pv FROM PrivateMessage pv
                    WHERE ((pv.recipient.id = :currentUserId) AND (pv.message.status = "SENT"))
                    ORDER BY pv.createdAt DESC
                    """)
    List<PrivateMessage> findAllOldPrivateMessage(Long currentUserId);

    @Query("""
            SELECT COUNT(pv)
            FROM PrivateMessage pv
            WHERE (pv.sender.id = :currentUserId OR pv.recipient.id = :currentUserId)
            """)
    Integer findUnread(Long currentUserId, MessageStatus status);
}
