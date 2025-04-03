package br.com.gabxdev.repository;

import br.com.gabxdev.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
