package com.messagingservice.core.exception;

public class UserCannotBlockThemselfException extends RuntimeException {
    public UserCannotBlockThemselfException(String message){
        super(message);
    }
}
