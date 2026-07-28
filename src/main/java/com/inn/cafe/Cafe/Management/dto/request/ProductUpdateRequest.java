package com.inn.cafe.Cafe.Management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductUpdateRequest(
        @NotNull(message = "id should be mandatory")
        Integer id,

        String name,

        Integer categoryId,

        String description,

        Integer price,

        String status
) {
}
