package com.inn.cafe.Cafe.Management.dto.response;

public record ProductBasicResponse(
        Integer id,
        String name,
        String description,
        Integer price
) {}
