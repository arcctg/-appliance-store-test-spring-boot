package com.epam.rd.autocode.assessment.appliances.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("User with email %s already exists".formatted(email));
    }
}
