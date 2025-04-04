package br.com.gabxdev.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserBlockedException extends ResponseStatusException {
    public UserBlockedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
