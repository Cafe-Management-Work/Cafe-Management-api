package com.inn.cafe.Cafe.Management.constants;

public final class ExceptionConstants {

    //Error
    public static  final String ERROR = "error";

    // -------- VALIDATION ----------
    public static final String VALIDATION_FAILED = "Validation failed";

    // -------- AUTHENTICATION / AUTHORIZATION ----------
    public static final String WRONG_PASSWORD = "Wrong password";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String AUTHENTICATION_REQUIRED = "Please authenticate the user";
    public static final String ACCESS_DENIED = "You are not authorized to access this resource";
    public static final String ADMIN_ACCESS_ONLY = "ONLY ADMIN CAN ACCESS";

    // -------- USER ----------
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USER_NOT_AVAILABLE = "User not available";

    // -------- CATEGORY ----------
    public static final String CATEGORY_NOT_FOUND = "Category not found";

    // -------- REQUEST / API ----------
    public static final String REQUEST_BODY_MISSING =
            "Request body is missing or invalid. Please provide valid JSON input";

    public static final String PATH_VARIABLE_MISSING =
            "Path variable '%s' is mandatory";

    public static final String INVALID_URL =
            "Invalid URL or missing required path parameter";

    // -------- PASSWORD ----------
    public static final String PASSWORD_MISMATCH = "Old Password not matches with database records";

    public static final String UUID_MISSING ="Please enter UUID";

    private ExceptionConstants() {
        // prevent instantiation
    }
}
