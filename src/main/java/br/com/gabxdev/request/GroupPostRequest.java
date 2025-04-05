package br.com.gabxdev.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record GroupPostRequest(

        @NotBlank
        String name,

        @NotBlank
        String description,

        Set<Long> usersId
) {
}
