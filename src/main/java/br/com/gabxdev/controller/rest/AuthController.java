package br.com.gabxdev.controller.rest;

import br.com.gabxdev.commons.HeaderUtils;
import br.com.gabxdev.dto.request.LoginPostRequest;
import br.com.gabxdev.dto.request.RegisterPostRequest;
import br.com.gabxdev.dto.response.RegisterPostResponse;
import br.com.gabxdev.dto.response.TokenJwtResponse;
import br.com.gabxdev.mapper.UserMapper;
import br.com.gabxdev.security.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserMapper userMapper;

    private final HeaderUtils headerUtils;

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterPostResponse> registerUser(@Valid @RequestBody RegisterPostRequest request) {
        log.debug("Received request to register user");

        var savedUser = authService.registerUser(userMapper.toEntity(request));

        var headerLocation = headerUtils.createHeaderLocation(savedUser.getId().toString());

        return ResponseEntity.created(headerLocation).body(userMapper.toRegisterPostResponse(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenJwtResponse> loginUser(@Valid @RequestBody LoginPostRequest request) {
        log.debug("Received request to login user");

        var tokenJwt = authService.loginUser(request.email(), request.password());

        log.debug("Logged in user successfully with token {}", tokenJwt);

        return ResponseEntity.ok().body(tokenJwt);
    }

}
