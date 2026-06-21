package com.projects.flipkart.CustomExceptions;
// This is a common way to define a custom exception

public class ApiException extends Exception {

    // Constructor that takes a message
    public ApiException(String message) {
        super(message);
    }

    // Constructor that wraps another exception (like your code does)
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}