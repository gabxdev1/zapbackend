package br.com.gabxdev.repository;

import br.com.gabxdev.model.Friendship;
import br.com.gabxdev.model.pk.FriendshipId;
import br.com.gabxdev.response.projection.FriendshipGetProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

    Page<FriendshipGetProjection> findById_UserId2OrId_UserId1(Long idUserId2, Long idUserId1, Pageable pageable);

    @Query("""
            SELECT f FROM Friendship f
            WHERE (f.user1.id = : idUserId1 AND f.user2.id = : idUserId2) OR
            (f.user1.id = :idUserId2 AND f.user2.id = :idUserId1)
            """)
    Optional<Friendship> findFriendshipById(Long idUserId2, Long idUserId1);

}
