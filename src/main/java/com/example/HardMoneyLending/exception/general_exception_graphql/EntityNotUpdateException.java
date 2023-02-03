package com.example.HardMoneyLending.exception.general_exception_graphql;

import com.example.HardMoneyLending.exception.AbstractGraphQLException;

import java.util.Map;

public class EntityNotUpdateException extends AbstractGraphQLException {

    public EntityNotUpdateException(String message) {
        super(message);
    }

    public EntityNotUpdateException(String message, Map<String, Object> additionParams) {
        super(message, additionParams);
    }
}
