package br.com.gabxdev.repository;

import br.com.gabxdev.model.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {


    @Query("""
                SELECT DISTINCT g
                FROM Group g
                JOIN FETCH g.members m
                WHERE g.id IN (
                    SELECT gm.group.id
                    FROM GroupMember gm
                    WHERE gm.user.id = :userId
                )
            """)
    List<Group> findAllGroupsFullDetailsForUser(Long userId);


    @EntityGraph(value = "Group.fullDetails", type = EntityGraph.EntityGraphType.FETCH)
    @Query("""
            SELECT g
            FROM Group g
            WHERE g.id = :groupId
            """)
    Optional<Group> findGroupByIdFullDetails(Long groupId);
}
