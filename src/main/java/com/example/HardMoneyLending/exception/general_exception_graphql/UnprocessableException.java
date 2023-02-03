package com.example.HardMoneyLending.exception.general_exception_graphql;

import com.example.HardMoneyLending.exception.AbstractGraphQLException;

import java.util.Map;

public class UnprocessableException extends AbstractGraphQLException {

    public UnprocessableException(String message){super(message);}

    public UnprocessableException(String message, Map<String, Object> additionParams) {
        super(message, additionParams);
    }
}
