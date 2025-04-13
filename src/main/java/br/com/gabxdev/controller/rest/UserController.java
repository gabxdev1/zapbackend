package br.com.gabxdev.controller.rest;

import br.com.gabxdev.anotations.CurrentUser;
import br.com.gabxdev.dto.request.user.UserPutRequest;
import br.com.gabxdev.dto.response.privateMessage.PrivateMessageGetResponse;
import br.com.gabxdev.dto.response.user.UserGetResponse;
import br.com.gabxdev.dto.response.user.UserPutResponse;
import br.com.gabxdev.mapper.UserMapper;
import br.com.gabxdev.model.User;
import br.com.gabxdev.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserService service;

    private final UserMapper mapper;

    @GetMapping(path = "/search", headers = {HttpHeaders.AUTHORIZATION})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<UserGetResponse>> findUserByEmail(@RequestParam String email, @CurrentUser User currentUser) {

        var user = service.findByEmailLikeLimit20(email, currentUser);

        return ResponseEntity.ok(mapper.toUserGetResponseList(user));
    }


    @GetMapping(path = "/me", headers = {HttpHeaders.AUTHORIZATION})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserGetResponse> findUserById(@CurrentUser User currentUser) {

        var user = service.findByIdOrThrowNotFound(currentUser.getId());

        return ResponseEntity.ok(mapper.toUserGetResponse(user));
    }

    @PutMapping(path = "/me", headers = {HttpHeaders.AUTHORIZATION})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserPutResponse> updatedUser(@Valid @RequestBody UserPutRequest request) {
        log.debug("Update user: {}", request);

        var userUpdated = service.update(mapper.toEntity(request));

        return ResponseEntity.ok(mapper.toUserPutResponse(userUpdated));
    }
}
