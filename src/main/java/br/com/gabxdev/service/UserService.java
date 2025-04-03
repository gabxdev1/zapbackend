package br.com.gabxdev.service;

import br.com.gabxdev.exception.EmailAlreadyExistsException;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.User;
import br.com.gabxdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(id)));
    }

    public User update(User updateUser) {
        assertEmailDoesNotExists(updateUser.getEmail(), updateUser.getId());
        return repository.save(updateUser);
    }

    public void assertEmailDoesNotExists(String email, long id) {
        if (repository.existsByEmailIgnoreCaseAndIdNot(email, id))
            throw new EmailAlreadyExistsException("E-mail %s already exists".formatted(email));
    }
}
