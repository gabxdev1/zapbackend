package br.com.gabxdev.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandlerAdvice { //akarta.validation.UnexpectedTypeException:

    private ResponseEntity<ApiError> prepareResponse(final HttpStatus status, final HttpServletRequest request, final String message) {
        var path = request.getRequestURI();

        var error = ApiError.builder()
                .status(status.value())
                .path(path)
                .error(status.getReasonPhrase())
                .message(message)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(final NotFoundException ex, final HttpServletRequest request) {
        return prepareResponse(HttpStatus.NOT_FOUND, request, ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleNotFoundException(final EmailAlreadyExistsException ex, final HttpServletRequest request) {
        return prepareResponse(HttpStatus.BAD_REQUEST, request, ex.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex,
                                                                          final HttpServletRequest request) {
        var errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return prepareResponse(HttpStatus.UNPROCESSABLE_ENTITY, request, errorMessages);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex, final HttpServletRequest request) {
        return prepareResponse(HttpStatus.BAD_REQUEST, request, ex.getMessage());
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<ApiError> handleJsonMappingException(final JsonMappingException ex, final HttpServletRequest request) {
        return prepareResponse(HttpStatus.BAD_REQUEST, request, ex.getMessage());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatchException(final TypeMismatchException ex, final HttpServletRequest request) {
        return prepareResponse(HttpStatus.BAD_REQUEST, request, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleForbiddenException(final AccessDeniedException ex, final HttpServletRequest request) {
        return prepareResponse(HttpStatus.FORBIDDEN, request, ex.getMessage());
    }

    @ExceptionHandler(UserBlockedException.class)
    public ResponseEntity<ApiError> handleUserBlockedException(final UserBlockedException ex, final HttpServletRequest request) {
        return prepareResponse(HttpStatus.FORBIDDEN, request, ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbiddenException(final ForbiddenException ex, final HttpServletRequest request) {
        return prepareResponse(HttpStatus.FORBIDDEN, request, ex.getMessage());
    }


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleException(final Exception ex, final HttpServletRequest request) {
//        return prepareResponse(HttpStatus.INTERNAL_SERVER_ERROR, request, ex.getMessage());
//    }
}
