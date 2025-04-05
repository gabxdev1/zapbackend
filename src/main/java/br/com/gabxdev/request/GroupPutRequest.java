package br.com.gabxdev.request;

import jakarta.validation.constraints.NotBlank;

public record GroupPutRequest(

        @NotBlank
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String description
) {
}
