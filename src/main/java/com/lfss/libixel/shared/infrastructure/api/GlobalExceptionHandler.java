package com.lfss.libixel.shared.infrastructure.api;

import com.lfss.libixel.shared.exceptions.EmailAlreadyUsedException;
import com.lfss.libixel.shared.exceptions.UsernameAlreadyUsedException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationError(MethodArgumentNotValidException ex) {
        List<ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ProblemDetail problem = ProblemFactory.validation(errors);

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(UsernameAlreadyUsedException.class)
    public ResponseEntity<ProblemDetail> handleUsernameAlreadyUsedError(UsernameAlreadyUsedException ex) {
        ValidationError error = new ValidationError("username", ex.getMessage());
        ProblemDetail problem = ProblemFactory.alreadyExists(error);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ProblemDetail> handleEmailAlreadyUsedError(EmailAlreadyUsedException ex) {
        ValidationError error = new ValidationError("email", ex.getMessage());
        ProblemDetail problem = ProblemFactory.alreadyExists(error);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Throwable cause = ex.getMostSpecificCause();

        if (cause instanceof ConstraintViolationException cve) {
            String constraint = cve.getConstraintName();
            if (constraint == null) { throw ex; }

            ValidationError error = switch (constraint) {
                case "users_username_key" -> new ValidationError("username", "Username is already in use.");
                case "users_email_key" -> new ValidationError("email", "Email is already in use.");
                default -> throw ex;
            };

            ProblemDetail problem = ProblemFactory.alreadyExists(error);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
        }

        throw ex;
    }
}
