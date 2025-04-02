package br.com.gabxdev.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
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
import java.time.OffsetDateTime;
import java.util.Date;
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
                .timestamp(OffsetDateTime.now())
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
                .timestamp(OffsetDateTime.now())
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
                .timestamp(OffsetDateTime.now())
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
                .timestamp(OffsetDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    //UnsupportedJwtException
    //JwtException
    //ExpiredJwtException


    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiError> handleExpiredJwtException(final SignatureException ex, final HttpServletRequest request) {
        var path = request.getRequestURI();

        var error = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(path)
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message("JWT signature does not is validated")
                .timestamp(OffsetDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleExpiredJwtException(final ExpiredJwtException ex, final HttpServletRequest request) {
        var path = request.getRequestURI();

        var expiration = ex.getClaims().getExpiration();
        var expiredMs = Date.from(Instant.now()).getTime() - ex.getClaims().getIssuedAt().getTime();

        var message = "Token expired %d milliseconds ago at %s.".formatted(expiredMs, expiration.toString());

        var error = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(path)
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(message)
                .timestamp(OffsetDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
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
                .timestamp(OffsetDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
