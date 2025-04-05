package br.com.gabxdev.repository;

import br.com.gabxdev.model.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {


    @EntityGraph(value = "Group.fullDetails")
    @Query("""
            SELECT g FROM Group g
            WHERE g.id = :id
            """)
    Optional<Group> findByIdFullDetails(Long id);
}
