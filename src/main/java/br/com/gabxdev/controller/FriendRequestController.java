package br.com.gabxdev.controller;

import br.com.gabxdev.mapper.FriendRequestMapper;
import br.com.gabxdev.request.FriendRequestDeleteRequest;
import br.com.gabxdev.request.FriendRequestPostRequest;
import br.com.gabxdev.request.FriendRequestPutRequest;
import br.com.gabxdev.service.FriendRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/friends-requests")
@Slf4j
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService service;

    private final FriendRequestMapper mapper;

    @PostMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("(#request.senderId() == authentication.principal.id && hasRole('USER')) || hasRole('ADMIN')")
    public ResponseEntity<Void> sendFriendRequest(@Valid @RequestBody FriendRequestPostRequest request) {
        log.debug("Received request to send friend request");

        service.sendFriendRequest(mapper.toFriendRequestId(request));

        return ResponseEntity.noContent().build();
    }

    @PutMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("(#request.receiverId() == authentication.principal.id && hasRole('USER')) || hasRole('ADMIN')")
    public ResponseEntity<Void> acceptFriendRequest(@Valid @RequestBody FriendRequestPutRequest request) {
        log.debug("Received request to accept friend request");

        service.acceptFriendRequest(mapper.toFriendRequestId(request));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("(#request.receiverId() == authentication.principal.id && hasRole('USER')) || hasRole('ADMIN')")
    public ResponseEntity<Void> rejectFriendRequest(@Valid @RequestBody FriendRequestDeleteRequest request) {
        log.debug("Received request to reject friend request");

        service.rejectFriendRequest(mapper.toFriendRequestId(request));

        return ResponseEntity.noContent().build();
    }
}
