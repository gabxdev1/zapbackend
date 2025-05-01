package br.com.gabxdev.repository;

import br.com.gabxdev.model.PrivateMessage;
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
                    """)
    List<PrivateMessage> findAllPrivateMessage(Long currentUserId);

    @Query(
            """
                    SELECT pv FROM PrivateMessage pv
                    WHERE ((pv.recipient.id = :currentUserId) AND (pv.message.status = "SENT"))
                    """)
    List<PrivateMessage> findAllOldPrivateMessage(Long currentUserId);
}
