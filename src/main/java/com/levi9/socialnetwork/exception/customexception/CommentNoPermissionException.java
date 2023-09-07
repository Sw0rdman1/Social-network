package com.levi9.socialnetwork.exception.customexception;

public class CommentNoPermissionException extends RuntimeException {
    public CommentNoPermissionException(String message) {
        super(message);
    }
}
