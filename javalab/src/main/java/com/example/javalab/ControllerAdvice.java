package com.example.javalab;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(CustomException.class) //custom exception RCA
    public ResponseMessage handleCustomException(CustomException exception) //This was surprisingly simple to set up
    {
        ResponseMessage responseMessage = new ResponseMessage(exception.getMessage()); //Returning a custom ResponseMessage object
        return responseMessage;
    }

}
