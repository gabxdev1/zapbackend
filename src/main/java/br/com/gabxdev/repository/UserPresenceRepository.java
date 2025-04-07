package br.com.gabxdev.repository;

import br.com.gabxdev.model.UserPresence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPresenceRepository extends JpaRepository<UserPresence, Long> {
}
