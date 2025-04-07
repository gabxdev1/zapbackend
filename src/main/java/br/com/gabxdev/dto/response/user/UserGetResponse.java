package br.com.gabxdev.dto.response.user;

public record UserGetResponse(
        Long id,

        String firstName,

        String lastName,

        String email
) {
}
