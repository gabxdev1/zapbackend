package br.com.gabxdev.security.services;

import br.com.gabxdev.exception.EmailAlreadyExistsException;
import br.com.gabxdev.model.User;
import br.com.gabxdev.properties.ZapBackendProperties;
import br.com.gabxdev.repository.UserRepository;
import br.com.gabxdev.response.TokenJwtResponse;
import br.com.gabxdev.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomDetailsService customDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ZapBackendProperties properties;
    private final UserRepository userRepository;

    public User registerUser(User newUser) {
        assertEmailDoesNotExists(newUser.getEmail());

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(newUser);
    }

    public TokenJwtResponse loginUser(String email, String password) {
        var user = (User) customDetailsService.loadUserByUsername(email);

        if (!passwordEncoder.matches(password.trim(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        var accessToken = jwtUtil.generateToken(user.getId(), user.getRole());

        var expirationSeconds = properties.getJwt().getExpirationSeconds();

        return TokenJwtResponse.builder()
                .accessToken(accessToken)
                .expiresIn(expirationSeconds)
                .tokenType("Bearer")
                .build();
    }

    public void assertEmailDoesNotExists(String email) {
        if (userRepository.existsByEmailIgnoreCase(email))
            throw new EmailAlreadyExistsException("E-mail %s already exists".formatted(email));
    }
}
