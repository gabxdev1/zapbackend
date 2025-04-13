package br.com.gabxdev.repository;

import br.com.gabxdev.model.PrivateMessage;
import br.com.gabxdev.model.User;
import br.com.gabxdev.model.enums.MessageStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, UUID> {

    Slice<PrivateMessage> findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByCreatedAtDesc(
            Long senderId1, Long recipientId1,
            Long senderId2, Long recipientId2,
            Pageable pageable
    );

    @Query(
            """
            SELECT pv FROM PrivateMessage pv
            WHERE (pv.sender.id = :currentUserId AND pv.recipient.id = :friendId) OR (pv.recipient.id = :currentUserId AND pv.sender.id = :friendId)
            ORDER BY pv.createdAt DESC
            LIMIT 1
            """
    )
    Optional<PrivateMessage> findAllMessageByCurrentUserAndFriend(Long currentUserId, Long friendId);

    @Query("""
            SELECT COUNT(pv)
            FROM PrivateMessage pv
            WHERE (pv.sender.id = :currentUserId OR pv.recipient.id = :currentUserId)
            """)
    Integer findUnread(Long currentUserId, MessageStatus status);
}
