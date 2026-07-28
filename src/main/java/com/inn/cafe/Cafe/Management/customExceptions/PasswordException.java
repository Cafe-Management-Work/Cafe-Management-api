package com.inn.cafe.Cafe.Management.customExceptions;

public class PasswordException extends  RuntimeException{
    public PasswordException(String message) {
        super(message);
    }
}
