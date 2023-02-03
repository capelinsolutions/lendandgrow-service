package com.example.HardMoneyLending.exception.general_exception_graphql;

import com.example.HardMoneyLending.exception.AbstractGraphQLException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class EntityNotFoundException extends AbstractGraphQLException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Map<String, Object> additionParams) {
        super(message, additionParams);
        additionParams.put("status", HttpStatus.NOT_FOUND.value());
        additionParams.put("code", HttpStatus.NOT_FOUND.toString());
    }
}
