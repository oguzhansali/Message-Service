package com.example.Messaging.Service.core.exception;

public class BlockerUserNotFoundException extends RuntimeException{
    public BlockerUserNotFoundException(String message){
        super(message);
    }
}
