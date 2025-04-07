package br.com.gabxdev.dto.request.group;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GroupPutRequest(

        @NotNull
        @Min(1)
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String description
) {
}
