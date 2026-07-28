package com.inn.cafe.Cafe.Management.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ForgotPassword(
        @NotBlank(message = "Email should not be blank")
        String email
) {
}
