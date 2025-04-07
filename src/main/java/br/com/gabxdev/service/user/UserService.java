package br.com.gabxdev.service.user;

import br.com.gabxdev.exception.EmailAlreadyExistsException;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.User;
import br.com.gabxdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final SimpMessagingTemplate messagingTemplate;

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(id)));
    }

    public User update(User updateUser) {
        var userToUpdate = findByIdOrThrowNotFound(updateUser.getId());

        assertEmailDoesNotExists(updateUser.getEmail(), updateUser.getId());

        userToUpdate.setFirstName(updateUser.getFirstName());
        userToUpdate.setLastName(updateUser.getLastName());
        userToUpdate.setEmail(updateUser.getEmail());

        if (updateUser.getPassword() != null) {
            userToUpdate.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }

        return repository.save(userToUpdate);
    }

    public void assertEmailDoesNotExists(String email, long id) {
        if (repository.existsByEmailIgnoreCaseAndIdNot(email, id))
            throw new EmailAlreadyExistsException("E-mail %s already exists".formatted(email));
    }

    public List<User> findByEmailLikeLimit20(String email, User currentUser) {
        var currentUserId = currentUser.getId();

        return repository.findFirst20ByEmailIgnoreCaseContainsAndIdNot(email, currentUserId);
    }
}
