package br.com.gabxdev.dto.response;

import lombok.Builder;

@Builder
public record TokenJwtResponse(
        String accessToken,
        String tokenType,
        Long expiresIn
) {
}
