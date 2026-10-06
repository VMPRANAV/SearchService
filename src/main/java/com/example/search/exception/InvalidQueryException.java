package com.example.search.exception;

public class InvalidQueryException extends RuntimeException{
    public InvalidQueryException(String message) {
        super(message);
    }
}
