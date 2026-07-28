package com.inn.cafe.Cafe.Management.dto.request;

import jakarta.validation.constraints.*;

public record UserRequestDTO(

        @NotBlank(message = "Name is mandatory")
        String name,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is mandatory")
        String email,

        @NotBlank(message = "Contact number is mandatory")
        @Size(min = 10, max = 10, message = "Contact number must be 10 digits")
        String contactNumber,

        @NotBlank(message = "Password is mandatory")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @NotBlank(message = "Role is mandatory")
        String role
) {}
