package com.example.HardMoneyLending.exception.general_exception_graphql;

import com.example.HardMoneyLending.exception.AbstractGraphQLException;

import java.util.Map;

public class EntityAlreadyExistException extends AbstractGraphQLException {

    public EntityAlreadyExistException(String message) {
        super(message);
    }

    public EntityAlreadyExistException(String message, Map<String, Object> additionParams) {
        super(message, additionParams);
    }
}
