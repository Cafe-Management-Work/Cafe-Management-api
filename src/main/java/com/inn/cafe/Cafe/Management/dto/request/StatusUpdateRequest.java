package com.inn.cafe.Cafe.Management.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(

        @NotNull(message = "User ID cannot be null")
        @Min(value = 1, message = "User ID must be greater than 0")
        Integer id,

        @NotNull(message = "Status cannot be blank")
        String status

) {}
