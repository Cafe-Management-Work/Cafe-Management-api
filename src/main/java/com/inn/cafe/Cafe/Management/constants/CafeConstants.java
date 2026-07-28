package com.inn.cafe.Cafe.Management.constants;

public class CafeConstants {


    // ================= USER =================
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists: ";
    public static final String USER_SAVED_SUCCESSFULLY = "User Saved Successfully";
    public static final String USER_UNAVAILABLE = "User not available";
    public static final String USER_NOT_FOUND = "User does not exist";
    public static final String USER_NOT_FOUND_EMAIL = "No User Found on email";

    // ================= PASSWORD =================
    public static final String INCORRECT_PASSWORD = "Incorrect password";
    public static final String PASSWORD_MISMATCH = "Password Mismatch";
    public static final String PASSWORD_UPDATED_SUCCESS = "Password Updated Successfully";

    // ================= AUTH / ACCESS =================
    public static final String ADMIN_ACCESS_ONLY = "ONLY ADMIN CAN ACCESS";
    public static final String ACCOUNT_NOT_APPROVED = "Account not approved by admin";

    // ================= STATUS =================
    public static final String STATUS_TRUE = "true";
    public static final String STATUS_FALSE = "false";
    public static final String STATUS_UPDATED_SUCCESS = "Status Updated Successfully";
    public static final String USER_STATUS_APPROVED = "approved";
    public static final String USER_STATUS_DEACTIVATED = "deactivated";


    // ================= TOKEN =================
    public static final String TOKEN_VALID_RESPONSE = "true";

    // ================= EMAIL SUBJECTS =================
    public static final String SUBJECT_ACCOUNT_STATUS = "Account Status Update";
    public static final String SUBJECT_ADMIN_NOTIFICATION = "User Status Changed";
    public static final String SUBJECT_FORGOT_PASSWORD = "Credentials by Cafe Management";

    // ================= EMAIL MESSAGES =================
    public static final String USER_STATUS_MAIL_TEMPLATE =
            "Dear %s,\n\nYour account has been %s by Admin (%s).\n\nThank you,\nCafe Management Team";

    public static final String ADMIN_STATUS_MAIL_TEMPLATE =
            "Admin %s updated status of user %s (%s) to %s.";

    public static final String CHECK_MAIL_MESSAGE = "Check your Mail";

    // ================= JSON =================
    public static final String TOKEN_JSON_PREFIX = "{ \"token\" : \"";
    public static final String TOKEN_JSON_SUFFIX = "\" }";



    // ---------- AUTHORIZATION MESSAGES ----------
    public static final String ADMIN_ACCESS = "ONLY ADMIN CAN ACCESS";


    // ---------- CATEGORY MESSAGES ----------
    public static final String CATEGORY_ADDED = "Category Added Successfully";
    public static final String CATEGORY_UPDATED = "Category Updated Successfully";
    public static final String CATEGORY_NOT_FOUND = "Category not found  with categoryId: %s";
    public static final String CATEGORY_ID_NOT_NULL = "Category id must not be null";


    // ---------- PRODUCT MESSAGES ----------
    public static final String PRODUCT_ADDED_SUCCESSFULLY =
            "Product added successfully";
    public static final String PRODUCT_NOT_FOUND = "Product not found with id : %s ";

    public static final String PRODUCT_UPDATED ="product updated successfully";

    public static final String PRODUCT_DELETED = "Product deleted successfully";

    public static final String PRODUCT_STATUS_UPDATED = "Product status updated successfully";



    public static final String BILL_DELETED ="Bill Deleted Successfully with id : %s";



    public static final String BILL_NOT_FOUND ="Bill not found  with id : %s";


    private CafeConstants() {
        // Prevent instantiation
    }
}
