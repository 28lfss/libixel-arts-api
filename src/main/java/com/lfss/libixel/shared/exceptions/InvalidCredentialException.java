package com.lfss.libixel.shared.exceptions;

public class InvalidCredentialException extends RuntimeException {

    public InvalidCredentialException() {
        super("Invalid credentials");
    }
}
