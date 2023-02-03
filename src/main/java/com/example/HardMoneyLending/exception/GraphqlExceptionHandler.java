package com.example.HardMoneyLending.exception;

import com.example.HardMoneyLending.exception.general_exception.*;
import com.example.HardMoneyLending.exception.general_exception.DateTimeException.DateNotValidException;
import graphql.GraphQLException;
import graphql.kickstart.spring.error.ThrowableGraphQLError;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;

@Component
public class GraphqlExceptionHandler {

    @ExceptionHandler(GraphQLException.class)
    public ThrowableGraphQLError handle(GraphQLException e) {
        return new ThrowableGraphQLError(e);
    }

    @ExceptionHandler(Exception.class)
    public ThrowableGraphQLError handle(Exception e) {
        return new ThrowableGraphQLError(e);
    }

    @ExceptionHandler(EntityNotSavedException.class)
    public ThrowableGraphQLError handle(EntityNotSavedException e) {
        return new ThrowableGraphQLError(e);
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ThrowableGraphQLError handle(EntityAlreadyExistException e) {
        return new ThrowableGraphQLError(e);
    }

    @ExceptionHandler(UnprocessableException.class)
    public ThrowableGraphQLError handle(UnprocessableException e) {
        return new ThrowableGraphQLError(e);
    }

    @ExceptionHandler(DateNotValidException.class)
    public ThrowableGraphQLError handle(DateNotValidException e) {
        return new ThrowableGraphQLError(e);
    }

    @ExceptionHandler(ParseException.class)
    public ThrowableGraphQLError handle(ParseException e) {
        return new ThrowableGraphQLError(e);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ThrowableGraphQLError handle(EntityNotFoundException e) {
        return new ThrowableGraphQLError(e);
    }

    @ExceptionHandler(BadRequestException.class)
    public ThrowableGraphQLError handle(BadRequestException e) {
        return new ThrowableGraphQLError(e);
    }
}
