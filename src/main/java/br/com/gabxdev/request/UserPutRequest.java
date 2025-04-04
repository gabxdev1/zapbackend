package br.com.gabxdev.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPutRequest(

        @NotBlank(message = "The field 'firstName' is required")
        String firstName,

        @NotBlank(message = "The field 'lastName' is required")
        @NotBlank
        String lastName,

        @NotBlank(message = "The field 'email' is required")
        @NotBlank
        @Email
        String email,

        @Size(min = 8, max = 50)
        String password
) {
}
