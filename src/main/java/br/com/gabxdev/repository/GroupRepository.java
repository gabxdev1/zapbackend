package br.com.gabxdev.repository;

import br.com.gabxdev.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByIdAndCreatedBy(Long id, Long createdBy);
}
