package br.com.gabxdev.response.user;

public record UserPutResponse(
        Long id,

        String firstName,

        String lastName,

        String email
) {
}
