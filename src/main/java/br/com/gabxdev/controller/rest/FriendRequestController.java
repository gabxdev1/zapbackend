package br.com.gabxdev.controller.rest;

import br.com.gabxdev.dto.request.friend_request.FriendRequestDeleteRequest;
import br.com.gabxdev.dto.request.friend_request.FriendRequestPostRequest;
import br.com.gabxdev.dto.request.friend_request.FriendRequestPutRequest;
import br.com.gabxdev.dto.response.projection.SentPendingFriendRequestProjection;
import br.com.gabxdev.mapper.FriendRequestMapper;
import br.com.gabxdev.notification.dto.ReceivedPendingFriendRequestNotifier;
import br.com.gabxdev.service.friend_request.FriendRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users/friends-requests")
@Slf4j
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService service;

    private final FriendRequestMapper mapper;


    @GetMapping(path = "/received/pending", headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ReceivedPendingFriendRequestNotifier>> getPendingReceivedRequests() {
        log.info("Received request to get pending received friend requests");

        var pendingReceivedRequests = service.getPendingReceivedRequests();

        var response = mapper.toReceivedPendingFriendRequestNotifier(pendingReceivedRequests);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/sent/pending", headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<SentPendingFriendRequestProjection>> getPendingSentRequests(Pageable pageable) {
        log.info("Received request to get pending sent requests");

        var pendingReceivedRequests = service.getPendingSentRequests(pageable);

        return ResponseEntity.ok(pendingReceivedRequests);
    }

    @PostMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    public ResponseEntity<Void> sendFriendRequest(@Valid @RequestBody FriendRequestPostRequest request) {
        log.debug("Received request to send friend request");

        service.sendFriendRequest(mapper.toFriendRequestId(request));

        return ResponseEntity.noContent().build();
    }

    @PutMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    public ResponseEntity<Void> acceptFriendRequest(@Valid @RequestBody FriendRequestPutRequest request) {
        log.debug("Received request to accept friend request");

        service.acceptFriendRequest(mapper.toFriendRequestId(request));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(headers = HttpHeaders.AUTHORIZATION)
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    public ResponseEntity<Void> rejectFriendRequest(@Valid @RequestBody FriendRequestDeleteRequest request) {
        log.debug("Received request to reject friend request");

        service.rejectFriendRequest(mapper.toFriendRequestId(request));

        return ResponseEntity.noContent().build();
    }
}
