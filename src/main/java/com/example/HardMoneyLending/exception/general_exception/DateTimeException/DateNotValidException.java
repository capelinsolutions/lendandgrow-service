package com.example.HardMoneyLending.exception.general_exception.DateTimeException;

public class DateNotValidException extends Exception {

    public DateNotValidException() {
    }

    public DateNotValidException(String message) {
        super(message);
    }

    public DateNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
