package com.example.n8engine.exception;

public class SearchTypeNotFoundException extends Exception {
    public SearchTypeNotFoundException(String message) {
        super(message);
    }

    public SearchTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
