package com.inn.cafe.Cafe.Management.customExceptions;

public class UserNotExists extends RuntimeException{
    public UserNotExists(String message) {
        super(message);
    }
}
