package com.inn.cafe.Cafe.Management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates Getters, Setters, toString, equals, and hashCode
@AllArgsConstructor // Generates constructor with all fields
@NoArgsConstructor  // Generates default constructor (required for JSON mapping)
public class BillGenerateRequest {

    @NotBlank(message = "Please enter name of customer")
    private String name;

    @NotBlank(message = "Please enter customer email")
    private String email;

    @NotBlank(message = "Please enter customer contact number")
    @Size(min = 10,max = 10)
    private String contactNumber;

    @NotBlank(message = "Please enter payment method")
    private String paymentMethod;

    @NotBlank(message = "Please enter total bill")
    private String total;

    @NotNull(message = "Please fix the type of report type or old or new")
    private Boolean isGenerate;

    @NotBlank(message = "Please enter product details")
    private String productDetails;

    private String uuid;
}
