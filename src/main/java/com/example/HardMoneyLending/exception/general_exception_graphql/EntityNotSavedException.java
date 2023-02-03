package com.example.HardMoneyLending.exception.general_exception_graphql;

import com.example.HardMoneyLending.exception.AbstractGraphQLException;

import java.util.Map;

public class EntityNotSavedException extends AbstractGraphQLException {

    public EntityNotSavedException(String message) {
        super(message);
    }

    public EntityNotSavedException(String message, Map<String, Object> additionParams) {
        super(message, additionParams);
    }
}
