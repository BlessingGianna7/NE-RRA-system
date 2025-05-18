package com.naome.template.vehicle;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naome.template.history.HistoryRepository;
import com.naome.template.vehicle.dto.RegisterVehicleRequestDTO;
import com.naome.template.vehicle.dto.VehicleResponseDTO;
import com.naome.template.vehicle.dto.VehicleTransferRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final HistoryRepository historyRepository;

    //ROLE_ADMIN,
    // Register a new vehicle
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<VehicleResponseDTO> registerVehicle(
            @Valid @RequestBody RegisterVehicleRequestDTO requestDTO
    ) {
        VehicleResponseDTO responseDTO = vehicleService.registerVehicle(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Get a vehicle by ID
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> getVehicleById(@PathVariable UUID id) {
        VehicleResponseDTO vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }

    // Get all vehicles
    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> getAllVehicles() {
        List<VehicleResponseDTO> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Transfer vehicle ownership", description = "Transfer a vehicle to a new owner with a new plate number")
    @ApiResponse(responseCode = "200", description = "Vehicle transferred successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - Admin access required")
    @ApiResponse(responseCode = "404", description = "Vehicle or new owner not found")
    public ResponseEntity<String> transferVehicle(@RequestBody @Valid VehicleTransferRequest request) {
        vehicleService.transferVehicle(request);
        return ResponseEntity.ok("Vehicle transferred successfully");
    }

}
