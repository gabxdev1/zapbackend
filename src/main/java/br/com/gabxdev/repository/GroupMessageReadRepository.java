package br.com.gabxdev.repository;

import br.com.gabxdev.model.GroupMessageRead;
import br.com.gabxdev.model.pk.GroupMessageReadId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMessageReadRepository extends JpaRepository<GroupMessageRead, GroupMessageReadId> {
}
