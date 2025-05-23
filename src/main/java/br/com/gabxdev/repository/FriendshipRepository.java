package br.com.gabxdev.repository;

import br.com.gabxdev.dto.response.FriendshipGetResponse;
import br.com.gabxdev.model.Friendship;
import br.com.gabxdev.model.User;
import br.com.gabxdev.model.pk.FriendshipId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {


    @Query("""
            SELECT
                new br.com.gabxdev.dto.response.FriendshipGetResponse(
                    CASE
                        WHEN f.user1.id = :currentUserId THEN f.user2.id
                        ELSE f.user1.id
                    END,
                    CASE
                        WHEN f.user1.id = :currentUserId THEN f.user2.firstName
                        ELSE f.user1.firstName
                    END,
                    CASE
                        WHEN f.user1.id = :currentUserId THEN f.user2.lastName
                        ELSE f.user1.lastName
                    END,
                    CASE
                        WHEN f.user1.id = :currentUserId THEN f.user2.nickname
                        ELSE f.user1.nickname
                    END,
                    CASE
                        WHEN f.user1.id = :currentUserId THEN f.user2.presence.lastSeenAt
                        ELSE f.user1.presence.lastSeenAt
                    END,
                    f.createdAt
                )
            FROM Friendship f
            WHERE (f.user1.id = :currentUserId OR f.user2.id = :currentUserId)
            """)
    Page<FriendshipGetResponse> findAllFriendshipByUserIdPaginated(Long currentUserId, Pageable pageable);

    @Query("""
            SELECT
                CASE
                    WHEN f.user1.id = :currentUserId THEN f.user2.email
                    ELSE f.user1.email
                END
            FROM Friendship f
            WHERE (f.user1.id = :currentUserId OR f.user2.id = :currentUserId)
            """)
    List<String> findAllEmailFriendshipByUserId(Long currentUserId);

    @Query("""
                SELECT f.user2 FROM Friendship f WHERE f.user1.id = :currentUserId
                UNION
                SELECT f.user1 FROM Friendship f WHERE f.user2.id = :currentUserId
            """)
    List<User> findFriendsOf(Long currentUserId);

    @Query("""
            SELECT f FROM Friendship f
            WHERE (f.user1.id = : idUserId1 AND f.user2.id = : idUserId2) OR
            (f.user1.id = :idUserId2 AND f.user2.id = :idUserId1)
            """)
    Optional<Friendship> findFriendshipById(Long idUserId2, Long idUserId1);

}
