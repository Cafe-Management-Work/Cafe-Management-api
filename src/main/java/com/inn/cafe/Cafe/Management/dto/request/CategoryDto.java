package com.inn.cafe.Cafe.Management.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(
        @NotBlank(message = "Name Should not be blank")
        String name
) {
}
