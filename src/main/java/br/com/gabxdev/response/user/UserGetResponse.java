package br.com.gabxdev.response;

public record UserGetResponse(
        Long id,

        String firstName,

        String lastName,

        String email
) {
}
