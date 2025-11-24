package com.nikan.weblog.myException;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}