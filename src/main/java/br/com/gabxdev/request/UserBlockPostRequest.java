package br.com.gabxdev.request;

import jakarta.validation.constraints.Min;

public record UserBlockPostRequest(

        @Min(1)
        Long blocked
) {
}
