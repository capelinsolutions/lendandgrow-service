package com.example.HardMoneyLending.exception.general_exception;

public class BadRequestException extends Exception{

    public BadRequestException(){}

    public BadRequestException(String message){super(message);}

    public BadRequestException(String message, Throwable cause){super(message, cause);}
}
