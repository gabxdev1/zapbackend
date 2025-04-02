package br.com.gabxdev.exception;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record ApiError(

        OffsetDateTime timestamp,

        int status,

        String error,

        String message,

        String path
) {
}


