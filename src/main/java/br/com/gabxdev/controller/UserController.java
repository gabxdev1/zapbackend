package br.com.gabxdev.controller;

import br.com.gabxdev.commons.anotations.CurrentUser;
import br.com.gabxdev.mapper.UserMapper;
import br.com.gabxdev.model.User;
import br.com.gabxdev.request.UserPutRequest;
import br.com.gabxdev.response.UserGetResponse;
import br.com.gabxdev.response.UserPutResponse;
import br.com.gabxdev.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService service;

    private final UserMapper mapper;

    @GetMapping(headers = {HttpHeaders.AUTHORIZATION})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserGetResponse> findUserById(@CurrentUser User currentUser) {

        var user = service.findByIdOrThrowNotFound(currentUser.getId());

        return ResponseEntity.ok(mapper.toUserGetResponse(user));
    }

    @PutMapping(headers = {HttpHeaders.AUTHORIZATION})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserPutResponse> updatedUser(@Valid @RequestBody UserPutRequest request) {
        log.debug("Update user: {}", request);

        var userUpdated = service.update(mapper.toEntity(request));

        return ResponseEntity.ok(mapper.toUserPutResponse(userUpdated));
    }


}
