package br.com.gabxdev.controller;

import br.com.gabxdev.mapper.UserMapper;
import br.com.gabxdev.request.UserPutRequest;
import br.com.gabxdev.response.UserGetResponse;
import br.com.gabxdev.response.UserPutResponse;
import br.com.gabxdev.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService service;

    private final UserMapper mapper;

    @GetMapping(value = "/{id}", headers = {HttpHeaders.AUTHORIZATION})
    @PreAuthorize("(#id == authentication.principal.id && hasRole('USER')) || hasRole('ADMIN')")
    public ResponseEntity<UserGetResponse> findUserById(@PathVariable Long id) {
        log.debug("Find user by id: {}", id);

        var user = service.findByIdOrThrowNotFound(id);

        return ResponseEntity.ok(mapper.toUserGetResponse(user));
    }

    public ResponseEntity<UserPutResponse> updatedUser(UserPutRequest request) {
        log.debug("Update user: {}", request);

        var userUpdated = service.update(mapper.toEntity(request));

        return ResponseEntity.ok(mapper.toUserPutResponse(userUpdated));
    }


}
