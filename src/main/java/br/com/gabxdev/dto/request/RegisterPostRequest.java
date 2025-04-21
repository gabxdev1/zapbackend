package br.com.gabxdev.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterPostRequest(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        String nickname,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 50)
        String password
) {
}
