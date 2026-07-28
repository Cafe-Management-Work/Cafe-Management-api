package com.inn.cafe.Cafe.Management.dto.response;

public record UserResponse(
        Integer id,
        String name,
        String email,
        String contactNumber,
        String status

) {
}
