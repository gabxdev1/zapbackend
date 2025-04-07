package br.com.gabxdev.commons;

import br.com.gabxdev.exception.ForbiddenException;
import br.com.gabxdev.exception.NotFoundException;
import br.com.gabxdev.model.User;
import br.com.gabxdev.model.enums.Role;
import br.com.gabxdev.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static br.com.gabxdev.commons.Constants.NEW_USER;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final UserRepository repository;

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

    public void setAuthenticationContext(Authentication authentication) {
        if (authentication.getPrincipal() instanceof User) {
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

    public Authentication getAuthentication(Long userId, HttpServletRequest request) {
        var auth = createAuthentication(userId);

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return auth;
    }

    public Authentication getAuthentication(Long userId) {
        return createAuthentication(userId);
    }

    private UsernamePasswordAuthenticationToken createAuthentication(Long userId) {
        var user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(userId)));

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    public Authentication createAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    private boolean isNewUser(Authentication auth) {
        return !(auth.getPrincipal() instanceof User);
    }

    public void createAuthenticationAndSetAuthenticationContext(Long userId, String userEmail, List<String> roles) {
        var user = User.builder().id(userId).email(userEmail).role(Role.valueOf(roles.getFirst())).build();

        setAuthenticationContext(createAuthentication(user));
    }
}
