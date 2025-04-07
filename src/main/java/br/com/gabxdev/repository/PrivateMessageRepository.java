package br.com.gabxdev.repository;

import br.com.gabxdev.model.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, UUID> {
}
