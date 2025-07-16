package com.messagingservice.core.exception;

public class UserAlreadyBlockedException extends RuntimeException{
    public UserAlreadyBlockedException(String message){
        super(message);
    }
}
