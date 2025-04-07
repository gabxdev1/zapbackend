package br.com.gabxdev.dto.response;

public record RegisterPostResponse(
        Long id,

        String firstName,

        String lastName,

        String email
) {
}
