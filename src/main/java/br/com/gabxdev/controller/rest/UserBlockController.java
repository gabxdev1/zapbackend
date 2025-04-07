package br.com.gabxdev.controller.rest;

import br.com.gabxdev.dto.request.user.UserBlockDeleteRequest;
import br.com.gabxdev.dto.request.user.UserBlockPostRequest;
import br.com.gabxdev.service.user.UserBlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserBlockController {

    private final UserBlockService service;

    @PostMapping(path = "/block", headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> blockUser(@Valid @RequestBody UserBlockPostRequest request) {
        service.blockUser(request.blocked());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/unblock", headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> unblockUser(@Valid @RequestBody UserBlockDeleteRequest request) {
        service.unblockUser(request.blocked());

        return ResponseEntity.ok().build();
    }
}
