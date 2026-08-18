package com.lfss.libixel.shared.infrastructure.api;

import com.lfss.libixel.shared.exceptions.EmailAlreadyUsedException;
import com.lfss.libixel.shared.exceptions.InvalidCredentialException;
import com.lfss.libixel.shared.exceptions.UsernameAlreadyUsedException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationError(MethodArgumentNotValidException ex) {
        List<ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        return ProblemFactory.validation(errors);
    }

    @ExceptionHandler(UsernameAlreadyUsedException.class)
    public ProblemDetail handleUsernameAlreadyUsedError(UsernameAlreadyUsedException ex) {
        return ProblemFactory.alreadyExists(new ValidationError("username", ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ProblemDetail handleEmailAlreadyUsedError(EmailAlreadyUsedException ex) {
        return ProblemFactory.alreadyExists(new ValidationError("email", ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Throwable cause = ex.getMostSpecificCause();

        if (cause instanceof ConstraintViolationException cve) {
            String constraint = cve.getConstraintName();
            if (constraint == null) { throw ex; }

            ValidationError error = switch (constraint) {
                case "users_username_key" -> new ValidationError("username", "Username is already in use.");
                case "users_email_key" -> new ValidationError("email", "Email is already in use.");
                default -> throw ex;
            };

            return ProblemFactory.alreadyExists(error);
        }

        throw ex;
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ProblemDetail handleInvalidCredentials() {
        return ProblemFactory.invalidCredentials();
    }
}
