package com.naome.template.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VehicleResponseDTO(
        UUID id,
        String chassisNumber,
        String manufactureCompany,
        Integer manufactureYear,
        Double price,
        String modelName,
        UUID currentOwnerId,
        String plateNumber
) {
}
