package com.inn.cafe.Cafe.Management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdate(
        @NotBlank(message = "oldPassword is mandatory")
        String oldPassword,

        @NotBlank(message = "newPassword is mandatory")
        @Size(min = 6, message = "newPassword must be at least 6 characters")
        String newPassword
) {
}
