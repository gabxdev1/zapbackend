package br.com.gabxdev.controller;

import br.com.gabxdev.mapper.FriendshipMapper;
import br.com.gabxdev.request.FriendshipDeleteRequest;
import br.com.gabxdev.response.FriendshipGetResponse;
import br.com.gabxdev.service.FriendshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users/friendships")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService service;

    private final FriendshipMapper mapper;

    @GetMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<FriendshipGetResponse>> findAllFriendships(Pageable pageable) {
        var allFriendshipsPaginated = service.findAllFriendshipsPaginated(pageable);

        return ResponseEntity.ok().body(allFriendshipsPaginated);
    }

    @DeleteMapping(path = "/remove", headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> removeFriendship(@Valid @RequestBody FriendshipDeleteRequest request) {
        service.removeFriendshipIfExists(mapper.toFriendshipId(request));

        return ResponseEntity.ok().build();
    }
}
