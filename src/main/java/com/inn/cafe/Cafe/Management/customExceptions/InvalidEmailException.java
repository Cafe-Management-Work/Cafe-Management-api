package com.inn.cafe.Cafe.Management.customExceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }
}