package com.messagingservice.core.exception;

public class HaveBeenBlockedException extends RuntimeException{
    public HaveBeenBlockedException(String message){
        super(message);
    }
}
