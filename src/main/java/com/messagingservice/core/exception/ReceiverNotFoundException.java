package com.messagingservice.core.exception;

public class ReceiverNotFoundException extends RuntimeException{
    public ReceiverNotFoundException(String message){
        super(message);
    }
}
