package br.com.gabxdev.repository;

import br.com.gabxdev.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    List<User> findFirst20ByNicknameIgnoreCaseContainsAndIdNot(String username, Long id);

    @Query("SELECT u.email FROM User u WHERE u.id IN :ids")
    List<String> findAllEmailsByIdIn(@Param("ids") Set<Long> ids);

    @Query("SELECT u.email FROM User u WHERE u.id = :id")
    Optional<String> findEmailByUserId(@Param("id") Long id);
}
