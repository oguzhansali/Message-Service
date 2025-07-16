package com.messagingservice.core.exception;

public class BlockerUserNotFoundException extends RuntimeException{
    public BlockerUserNotFoundException(String message){
        super(message);
    }
}
