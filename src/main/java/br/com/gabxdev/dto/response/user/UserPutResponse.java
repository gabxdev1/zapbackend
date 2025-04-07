package br.com.gabxdev.dto.response.user;

public record UserPutResponse(
        Long id,

        String firstName,

        String lastName,

        String email
) {
}
