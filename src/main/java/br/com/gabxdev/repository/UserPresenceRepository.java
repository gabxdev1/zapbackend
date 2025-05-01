package br.com.gabxdev.repository;

import br.com.gabxdev.model.UserPresence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPresenceRepository extends JpaRepository<UserPresence, Long> {


    @Query("""
                SELECT up
                FROM UserPresence up
                WHERE up.user.id IN (
                    SELECT CASE
                               WHEN f.user1.id = :currentUserId THEN f.user2.id
                               ELSE f.user1.id
                           END
                    FROM Friendship f
                    WHERE (f.user1.id = :currentUserId OR f.user2.id = :currentUserId)
                          AND (f.user1.id <> f.user2.id)
                )
            """)
    List<UserPresence> findAllUserStatusRelatedCurrentUser(@Param("currentUserId") Long currentUserId);
}
