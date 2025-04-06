package br.com.gabxdev.response.user;

public record UserGetResponse(
        Long id,

        String firstName,

        String lastName,

        String email
) {
}
