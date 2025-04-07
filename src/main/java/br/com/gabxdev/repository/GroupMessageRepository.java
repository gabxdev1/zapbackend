package br.com.gabxdev.repository;

import br.com.gabxdev.model.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, UUID> {
}
