package br.com.gabxdev.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyExistsException extends ResponseStatusException {
    public EmailAlreadyExistsException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
