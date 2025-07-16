package com.example.Messaging.Service.core.exception;

public class SenderNotFoundException extends RuntimeException{
    public SenderNotFoundException(String message){
        super(message);
    }
}
