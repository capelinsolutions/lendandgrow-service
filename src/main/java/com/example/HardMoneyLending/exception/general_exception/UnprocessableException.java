package com.example.HardMoneyLending.exception.general_exception;

public class UnprocessableException extends Exception{

    public UnprocessableException(){}

    public UnprocessableException(String message){super(message);}

    public UnprocessableException(String message, Throwable cause){super(message, cause);}
}
