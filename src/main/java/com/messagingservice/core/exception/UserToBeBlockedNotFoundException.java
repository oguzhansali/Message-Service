package com.messagingservice.core.exception;

public class UserToBeBlockedNotFoundException extends RuntimeException{
    public UserToBeBlockedNotFoundException(String message){
        super(message);
    }
}
