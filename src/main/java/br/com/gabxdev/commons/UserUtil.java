package br.com.gabxdev.commons;

import br.com.gabxdev.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {
    public Long getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var userAuthenticated = (User) auth.getPrincipal();
        return userAuthenticated.getId();
    }
}
