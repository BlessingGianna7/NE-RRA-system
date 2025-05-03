package com.naome.template.history.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record VehicleTranserRequestDTO(
        @NotNull
        UUID vehicleId,

        @NotNull
        UUID newOwnerId,

        @NotBlank
        String newPlateNumber,

        @Positive
        Double purchasePrice
) {
}
