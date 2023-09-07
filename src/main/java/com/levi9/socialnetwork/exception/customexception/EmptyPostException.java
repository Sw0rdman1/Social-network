package com.levi9.socialnetwork.exception.customexception;

public class EmptyPostException extends RuntimeException {
    public EmptyPostException(String message) {
        super(message);
    }
}