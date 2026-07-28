package com.inn.cafe.Cafe.Management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotBlank(message = "Product name Should not be blank")
        String name,

        @NotNull(message = "categoryId should be mandatory")
        Integer categoryId,

        String description,

        @NotNull(message = "price should not be null")
        Integer price
) {
}
