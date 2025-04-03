package br.com.gabxdev.commons;

import br.com.gabxdev.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static br.com.gabxdev.commons.Constants.NEW_USER;

@Component
public class UserUtil {
    public Optional<Long> getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (isNewUser(auth)) {
            return Optional.of(NEW_USER);
        }

        var userAuthenticated = (User) auth.getPrincipal();
        return Optional.of(userAuthenticated.getId());
    }

    private boolean isNewUser(Authentication auth) {
        return !(auth.getPrincipal() instanceof User);
    }
}
