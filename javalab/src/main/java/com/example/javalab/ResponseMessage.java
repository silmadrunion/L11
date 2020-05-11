package com.example.javalab;

public class ResponseMessage {
    private String message; //really basic custom class with one variable and standard setter/getter/constructor

    public ResponseMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
