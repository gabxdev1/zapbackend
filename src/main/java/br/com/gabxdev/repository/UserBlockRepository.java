package br.com.gabxdev.repository;

import br.com.gabxdev.model.UserBlock;
import br.com.gabxdev.model.pk.UserBlockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserBlockRepository extends JpaRepository<UserBlock, UserBlockId> {

    @Query("""
            SELECT ub.isBlocked FROM UserBlock ub
            WHERE (ub.blocker.id = :idUserId1 AND ub.blocked.id = : idUserId2) OR
            (ub.blocker.id = :idUserId2 AND ub.blocked.id = :idUserId1)
            """)
    Optional<Boolean> isBlocked(Long idUserId1, Long idUserId2);
}
