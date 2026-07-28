package com.inn.cafe.Cafe.Management.customExceptions;

public class UserUnAuthorizedAccessException extends RuntimeException{
    public UserUnAuthorizedAccessException(String message) {
        super(message);
    }
}
