package br.com.gabxdev.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GroupPutRequest(

        @NotNull
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String description
) {
}
