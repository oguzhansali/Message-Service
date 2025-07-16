package com.example.Messaging.Service.core.exception;

public class ReceiverNotFoundException extends RuntimeException{
    public ReceiverNotFoundException(String message){
        super(message);
    }
}
