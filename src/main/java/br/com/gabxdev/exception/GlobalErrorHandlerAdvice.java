package br.com.gabxdev.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandlerAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex,
                                                                          final HttpServletRequest request) {
        var path = request.getRequestURI();

        var errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        var error = ApiError.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .path(path)
                .error(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
                .message(errorMessages)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex, final HttpServletRequest request) {
        var path = request.getRequestURI();

        var error = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .path(path)
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<ApiError> handleJsonMappingException(final JsonMappingException ex, final HttpServletRequest request) {
        var path = request.getRequestURI();

        var error = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .path(path)
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatchException(final TypeMismatchException ex, final HttpServletRequest request) {
        var path = request.getRequestURI();

        var error = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .path(path)
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(final Exception ex, final HttpServletRequest request) {
        var path = request.getRequestURI();

        var error = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(path)
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
