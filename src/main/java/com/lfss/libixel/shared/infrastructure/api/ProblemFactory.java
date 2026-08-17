package com.lfss.libixel.shared.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.List;

public final class ProblemFactory {

    private ProblemFactory() {}

    public static ProblemDetail validation(List<ValidationError> errors) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "One or more fields are invalid."
        );

        problem.setTitle("Validation failed");
        problem.setType(ProblemTypes.VALIDATION);
        problem.setProperty("errors", errors);

        return problem;
    }

    public static ProblemDetail alreadyExists(ValidationError error) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "A value is already in use."
        );

        problem.setTitle("Already exists");
        problem.setType(ProblemTypes.ALREADY_EXISTS);
        problem.setProperty("error", error);

        return problem;
    }

    public static ProblemDetail invalidCredentials() {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Username or password is incorrect."
        );

        problem.setTitle("Invalid credentials");
        problem.setType(ProblemTypes.INVALID_CREDENTIALS);

        return problem;
    }
}
