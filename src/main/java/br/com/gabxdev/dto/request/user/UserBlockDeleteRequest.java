package br.com.gabxdev.dto.request.user;

import jakarta.validation.constraints.Min;

public record UserBlockDeleteRequest(@Min(1) Long blocked) {
}
