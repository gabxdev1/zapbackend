package br.com.gabxdev.controller.rest;

import br.com.gabxdev.anotations.CurrentUser;
import br.com.gabxdev.mapper.UserPresenceMapper;
import br.com.gabxdev.model.User;
import br.com.gabxdev.notification.dto.UserPresenceStatusResponse;
import br.com.gabxdev.service.user.UserPresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/users/presence")
@RequiredArgsConstructor
public class UserPresenceController {

    private final UserPresenceService userPresenceService;

    private final UserPresenceMapper userPresenceMapper;

    @GetMapping(headers = {HttpHeaders.AUTHORIZATION})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<UserPresenceStatusResponse>> findAllUserPresence(@CurrentUser User currentUser) {

        var allUserStatusRelatedCurrentUser = userPresenceService.findAllUserStatusRelatedCurrentUser(currentUser.getId());

        var response = userPresenceMapper.toUserPresenceStatusResponseList(allUserStatusRelatedCurrentUser);

        return ResponseEntity.ok(response);
    }
}
