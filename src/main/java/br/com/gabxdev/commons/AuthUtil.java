package br.com.gabxdev.commons;

import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

import static br.com.gabxdev.commons.Constants.NEW_USER;

@Component
public class AuthUtil {
    public Optional<Long> getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (isNewUser(auth)) {
            return Optional.of(NEW_USER);
        }

        var userAuthenticated = (User) auth.getPrincipal();
        return Optional.of(userAuthenticated.getId());
    }

    public void setAuthenticationContext(Principal principal) {
        if (principal instanceof Authentication authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return;
        }

        throw new ForbiddenException("Authentication required");
    }

    public User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (isNewUser(auth)) {
            throw new ForbiddenException("You do not have permission to access this resource");
        }

        return (User) auth.getPrincipal();
    }

    private boolean isNewUser(Authentication auth) {
        return !(auth.getPrincipal() instanceof User);
    }
}
