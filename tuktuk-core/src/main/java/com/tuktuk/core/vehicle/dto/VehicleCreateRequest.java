package com.tuktuk.core.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleCreateRequest {

    @NotBlank(message = "Vehicle name is required")
    @Size(max = 100, message = "Vehicle name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Vehicle type is required")
    @Size(max = 50, message = "Vehicle type must be at most 50 characters")
    private String type;
}