package com.example.HardMoneyLending.utils;

import com.example.HardMoneyLending.exception.general_exception_graphql.EntityNotFoundException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class Message<T> {
    private int status;
    private String message;
    private String code;

    private T data;

    public Message() {
    }

    public Message(int status, String message, String code, T data) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public Message<T> setStatus(int status)
    {
        this.status = status;
        return this;
    }

    public Message<T> setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public Message<T> setData(T data)
    {
        this.data = data;
        return this;
    }

    public Message<T> setCode(String code)
    {
        this.code = code;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public Message<T> httpBadRequest(String message , T data){

        this.status = HttpStatus.BAD_REQUEST.value();
        this.code = HttpStatus.BAD_REQUEST.toString();
        this.data = data;
        this.message = message;
        return this;
    }
    public Message<T> httpCreated(String message , T data){

        this.status = HttpStatus.CREATED.value();
        this.code = HttpStatus.CREATED.toString();
        this.data = data;
        this.message = message;
        return this;
    }

    public Message<T> httpNotFound(String message , T data){
        this.status = HttpStatus.NOT_FOUND.value();
        this.code = HttpStatus.NOT_FOUND.toString();
        this.data = data;
        this.message = message;
        return this;
    }

    public Message<T> httpFound(String message , T data){
        this.status = HttpStatus.FOUND.value();
        this.code = HttpStatus.FOUND.toString();
        this.data = data;
        this.message = message;
        return this;
    }

    public Message<T> httpOk(String message , T data){
        this.status = HttpStatus.OK.value();
        this.code = HttpStatus.OK.toString();
        this.data = data;
        this.message = message;
        return this;
    }

    public Message<T> httpInternalServerError(){
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.toString();
        this.data = null;
        this.message = "Internal Server Error";
        return this;
    }

    public static <T> T getFromMessage(Message<T> data) throws EntityNotFoundException {
        if(data.getStatus() == HttpStatus.OK.value())
            return data.getData();
        throw new EntityNotFoundException("Not found.");
    }

    public static <T> List<T> getFromListMessage(ListMessage<T> data) throws EntityNotFoundException {
        if(data.getStatus() == HttpStatus.OK.value())
            return (List<T>)data.getData();
        throw new EntityNotFoundException("Not found.");
    }
}

