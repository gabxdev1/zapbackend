package br.com.gabxdev.dto.response.user;

public record UserGetDetailsResponse(
        Long id,

        String firstName,

        String lastName,

        String email,

        String nickname
) {
}
