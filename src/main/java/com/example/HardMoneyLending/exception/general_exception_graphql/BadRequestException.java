package com.example.HardMoneyLending.exception.general_exception_graphql;

import com.example.HardMoneyLending.exception.AbstractGraphQLException;

import java.util.Map;

public class BadRequestException extends AbstractGraphQLException {

    public BadRequestException(String message){super(message);}

    public BadRequestException(String message, Map<String, Object> additionParams) {
        super(message, additionParams);
    }
}
