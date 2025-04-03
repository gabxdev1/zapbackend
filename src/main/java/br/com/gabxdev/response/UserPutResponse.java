package br.com.gabxdev.response;

public record UserPutResponse(
        Long id,

        String firstName,

        String lastName,

        String email
) {
}
