package br.com.gabxdev.websocket.util;

import br.com.gabxdev.commons.AuthUtil;
import br.com.gabxdev.messaging.wrapper.MessageWrapper;
import br.com.gabxdev.messaging.wrapper.TriggerWrapper;
import br.com.gabxdev.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessagingWrapperUtil {

    private final AuthUtil authUtil;

    public <T> MessageWrapper<T> createMessageWrapper(T request, Principal principal) {
        var currentUser = extractUserAndSetAuthenticationContext(principal);

        var roles = extractRoles(currentUser.getAuthorities());

        return new MessageWrapper<>(request, currentUser.getId(), currentUser.getEmail(), roles);
    }

    public TriggerWrapper createTriggerWrapper(Principal principal) {
        var currentUser = extractUserAndSetAuthenticationContext(principal);

        var roles = extractRoles(currentUser.getAuthorities());

        return new TriggerWrapper(currentUser.getId(), currentUser.getEmail(), roles);
    }


    private List<String> extractRoles(Collection<? extends GrantedAuthority> roles) {
        return roles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private User extractUserAndSetAuthenticationContext(Principal principal) {
        authUtil.setAuthenticationContext(principal);

        return authUtil.getCurrentUser();
    }
}
