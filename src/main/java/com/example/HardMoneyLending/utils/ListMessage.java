package com.example.HardMoneyLending.utils;

import java.util.List;

public class ListMessage<T> {
    private int status;
    private String message;
    private String code;

    private List<T> data;

    public ListMessage() {
    }

    public ListMessage(int status, String message, String code, List<T> data) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public ListMessage<T> setStatus(int status)
    {
        this.status = status;
        return this;
    }

    public ListMessage<T> setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public ListMessage<T> setData(List<T> data)
    {
        this.data = data;
        return this;
    }

    public ListMessage<T> setCode(String code)
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

    public List<T> getData() {
        return data;
    }



}

