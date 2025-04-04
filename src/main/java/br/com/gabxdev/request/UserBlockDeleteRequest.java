package br.com.gabxdev.request;

import jakarta.validation.constraints.Min;

public record UserBlockDeleteRequest(@Min(1) Long blocked) {
}
