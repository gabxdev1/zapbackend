package br.com.gabxdev.repository;

import br.com.gabxdev.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    List<User> findFirst20ByEmailIgnoreCaseContains(@Param("email") String email);

    List<User> findFirst20ByEmailIgnoreCaseContainsAndIdNot(String email, Long id);
}
