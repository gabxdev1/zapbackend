package br.com.gabxdev.controller;

import br.com.gabxdev.mapper.UserMapper;
import br.com.gabxdev.request.LoginPostRequest;
import br.com.gabxdev.request.RegisterPostRequest;
import br.com.gabxdev.response.RegisterPostResponse;
import br.com.gabxdev.response.TokenJwtResponse;
import br.com.gabxdev.service.AuthService;
import br.com.gabxdev.util.HeaderUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserMapper userMapper;
    private final HeaderUtils headerUtils;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterPostResponse> registerUser(@Valid @RequestBody RegisterPostRequest request) {
        var savedUser = authService.registerUser(userMapper.toEntity(request));

        var headerLocation = headerUtils.createHeaderLocation(savedUser.getId().toString());

        return ResponseEntity.created(headerLocation).body(userMapper.toRegisterPostResponse(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenJwtResponse> loginUser(@Valid @RequestBody LoginPostRequest request) {
        var tokenJwt = authService.loginUser(request.email(), request.password());
        return ResponseEntity.ok().body(tokenJwt);
    }
}
