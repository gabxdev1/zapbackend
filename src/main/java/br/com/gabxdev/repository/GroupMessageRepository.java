package br.com.gabxdev.repository;

import br.com.gabxdev.model.GroupMessage;
import br.com.gabxdev.model.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Long> {
}
