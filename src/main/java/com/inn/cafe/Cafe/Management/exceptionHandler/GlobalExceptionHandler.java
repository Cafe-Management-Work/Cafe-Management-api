package com.inn.cafe.Cafe.Management.exceptionHandler;

import com.inn.cafe.Cafe.Management.constants.ExceptionConstants;
import com.inn.cafe.Cafe.Management.customExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------------- VALIDATION ----------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // ---------------- CUSTOM BUSINESS EXCEPTIONS ----------------
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex) {
        return build(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserUnAuthorizedAccessException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedAccess(
            UserUnAuthorizedAccessException ex) {
        return build(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotExists.class)
    public ResponseEntity<Map<String, String>> handleUserNotExists(
            UserNotExists ex) {
        return build(ExceptionConstants.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<Map<String, String>> handlePasswordException(
            PasswordException ex) {
        return build(ExceptionConstants.PASSWORD_MISMATCH, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoryNotFound(
            CategoryNotFoundException ex) {
        return build(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(
            ProductNotFoundException ex) {
        return build(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // ---------------- SECURITY EXCEPTIONS ----------------
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(
            BadCredentialsException ex) {
        return build(ExceptionConstants.WRONG_PASSWORD, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFound(
            UsernameNotFoundException ex) {
        return build(ExceptionConstants.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            InsufficientAuthenticationException.class,
            AuthenticationCredentialsNotFoundException.class
    })
    public ResponseEntity<Map<String, String>> handleAuthenticationRequired(
            Exception ex) {
        return build(ExceptionConstants.AUTHENTICATION_REQUIRED, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(PdfValidationException.class)
    public ResponseEntity<Map<String, String>> handlePdfProblems(PdfValidationException pdfValidationException){
        return build(pdfValidationException.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(
            AccessDeniedException ex) {
        return build(ExceptionConstants.ACCESS_DENIED, HttpStatus.FORBIDDEN);
    }

    // ---------------- REQUEST / API ERRORS ----------------
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Map<String, String>> handleMissingPathVariable(
            MissingPathVariableException ex) {

        String message = String.format(
                ExceptionConstants.PATH_VARIABLE_MISSING,
                ex.getVariableName()
        );

        return build(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleMissingRequestBody(
            HttpMessageNotReadableException ex) {
        return build(ExceptionConstants.REQUEST_BODY_MISSING, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoHandlerFound(
            NoHandlerFoundException ex) {
        return build(ExceptionConstants.INVALID_URL, HttpStatus.BAD_REQUEST);
    }

    // ---------------- COMMON RESPONSE BUILDER ----------------
    private ResponseEntity<Map<String, String>> build(String msg, HttpStatus status) {
        Map<String, String> error = new HashMap<>();
        error.put(ExceptionConstants.ERROR, msg);
        return new ResponseEntity<>(error, status);
    }
}
