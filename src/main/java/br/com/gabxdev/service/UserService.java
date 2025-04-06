package br.com.gabxdev.service;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.exception.EmailAlreadyExistsException;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.User;
import br.com.gabxdev.model.enums.UserStatus;
import br.com.gabxdev.repository.UserRepository;
import br.com.gabxdev.response.user.UserStatusNotifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final AuthUtil authUtil;

    private final SimpMessagingTemplate messagingTemplate;

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(id)));
    }

    public User findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(email)));
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

    public void updateLastSeen(Long userId) {
        repository.findById(userId)
                .ifPresent(user -> {
                    user.setLastSeen(Instant.now());
                    repository.save(user);
                });
    }

    public void assertEmailDoesNotExists(String email, long id) {
        if (repository.existsByEmailIgnoreCaseAndIdNot(email, id))
            throw new EmailAlreadyExistsException("E-mail %s already exists".formatted(email));
    }

    public List<User> findByEmailLikeLimit20(String email) {
        var currentUserId = authUtil.getCurrentUser().getId();

        return repository.findFirst20ByEmailIgnoreCaseContainsAndIdNot(email, currentUserId);
    }

    public void notifyUserLogin(Long userId) {
        var userStatusNotifyResponse = prepareUserStatusNotifyResponse(userId, UserStatus.ONLINE);

        var destination = "/topic/user/%d/status".formatted(userId);

        messagingTemplate.convertAndSend(destination, userStatusNotifyResponse);
    }

    public void notifyUserLogout(Long userId) {
        var userStatusNotifyResponse = prepareUserStatusNotifyResponse(userId, UserStatus.OFFLINE);

        var destination = "/topic/user/%d/status".formatted(userId);

        messagingTemplate.convertAndSend(destination, userStatusNotifyResponse);
    }

    public void updateUserStatus(Long userId, UserStatus newStatus) {
        var userStatusNotifyResponse = prepareUserStatusNotifyResponse(userId, newStatus);

        var destination = "/topic/user/%d/status".formatted(userId);

        messagingTemplate.convertAndSend(destination, userStatusNotifyResponse);
    }

    private UserStatusNotifyResponse prepareUserStatusNotifyResponse(Long userId, UserStatus newStatus) {
        var user = findByIdOrThrowNotFound(userId);

        user.setLastSeen(Instant.now());
        user.setStatus(UserStatus.OFFLINE);
        repository.save(user);

        return UserStatusNotifyResponse.builder()
                .userId(user.getId())
                .status(newStatus)
                .lastSeen(user.getLastSeen())
                .build();
    }
}
