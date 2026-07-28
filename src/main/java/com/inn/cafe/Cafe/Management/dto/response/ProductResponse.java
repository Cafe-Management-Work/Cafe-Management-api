package com.inn.cafe.Cafe.Management.dto.response;

public record ProductResponse(
        Integer id,
        String name,
        String description,
        Integer price,
        String status,
        Integer categoryId,
        String categoryName
) {
}
