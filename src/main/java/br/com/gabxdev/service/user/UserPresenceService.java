package br.com.gabxdev.service.user;

import br.com.gabxdev.model.enums.UserStatus;
import br.com.gabxdev.repository.UserPresenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserPresenceService {

    private final UserPresenceRepository userPresence;

    public void markOnline(Long userId) {
        log.debug("Marking user as ONLINE - ID: {}", userId);

        userPresence.findById(userId)
                .ifPresent(user -> {
                    user.setStatus(UserStatus.ONLINE);
                    userPresence.save(user);

                    log.debug("Status updated to ONLINE in the bank - ID: {}", userId);
                });
    }

    public void markOffline(Long userId, Instant lastSeenAt) {
        userPresence.findById(userId)
                .ifPresent(user -> {
                    user.setStatus(UserStatus.OFFLINE);
                    user.setLastSeenAt(lastSeenAt);

                    log.debug("Status updated to OFFLINE in the DB - ID: {}", userId);

                    userPresence.save(user);
                });
    }
}
