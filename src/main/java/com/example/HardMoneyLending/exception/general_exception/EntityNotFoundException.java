package com.example.HardMoneyLending.exception.general_exception;

public class EntityNotFoundException extends Exception{

    public EntityNotFoundException() {
    }
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
