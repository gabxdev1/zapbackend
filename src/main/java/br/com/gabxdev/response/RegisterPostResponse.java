package br.com.gabxdev.response;

public record RegisterPostResponse(
        Long id,

        String firstName,

        String lastName,

        String email
) {
}
