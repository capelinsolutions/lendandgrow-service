package com.example.HardMoneyLending.exception.general_exception;

public class EntityAlreadyExistException extends Exception{

    public EntityAlreadyExistException() {
    }

    public EntityAlreadyExistException(String message) {
        super(message);
    }

    public EntityAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
