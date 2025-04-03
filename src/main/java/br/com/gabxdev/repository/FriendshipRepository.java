package br.com.gabxdev.repository;

import br.com.gabxdev.model.Friendship;
import br.com.gabxdev.model.pk.FriendshipId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {
    boolean existsByIdAndBlockedIsTrue(FriendshipId id);
}
