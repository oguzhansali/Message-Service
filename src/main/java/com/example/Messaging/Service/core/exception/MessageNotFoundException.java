package com.example.Messaging.Service.core.exception;

public class MessageNotFoundException extends RuntimeException{
    public MessageNotFoundException(String message){
        super(message);
    }
}
