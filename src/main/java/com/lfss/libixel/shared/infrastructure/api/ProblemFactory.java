package com.lfss.libixel.shared.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.List;

public class ProblemFactory {

    public static ProblemDetail validation(List<ValidationError> errors) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Validation failed");
        problem.setDetail("One or more fields are invalid.");
        problem.setType(ProblemTypes.VALIDATION);
        problem.setProperty("errors", errors);

        return problem;
    }

    public static ProblemDetail alreadyExists(ValidationError error) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("Registration failed");
        problem.setDetail("Credential is already in use.");
        problem.setType(ProblemTypes.ALREADY_EXISTS);
        problem.setProperty("error", error);

        return problem;
    }
}
