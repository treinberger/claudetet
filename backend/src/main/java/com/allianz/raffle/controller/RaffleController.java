package com.allianz.raffle.controller;

import com.allianz.raffle.model.Raffle;
import com.allianz.raffle.service.RaffleService;
import com.allianz.raffle.exception.ValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/raffles")
@Tag(name = "Raffles", description = "Endpoints for managing raffles")
public class RaffleController {

    private final RaffleService raffleService;

    @Autowired
    public RaffleController(RaffleService raffleService) {
        this.raffleService = raffleService;
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get raffle by ID",
        description = "Retrieves a specific raffle by its ID"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the raffle")
    @ApiResponse(responseCode = "404", description = "Raffle not found")
    public ResponseEntity<?> getRaffleById(
            @Parameter(description = "ID of the raffle to retrieve")
            @PathVariable Long id) {

        return raffleService.getRaffleById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
        summary = "Create a new raffle",
        description = "Creates a new raffle with the provided details"
    )
    @ApiResponse(responseCode = "201", description = "Raffle created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid raffle data")
    public ResponseEntity<?> createRaffle(@RequestBody Raffle raffle) {
        try {
            Raffle createdRaffle = raffleService.createRaffle(raffle);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRaffle);
        } catch (ValidationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping
    @Operation(
        summary = "Get all raffles",
        description = "Retrieves all raffles that are visible to users (preview, active, or ended)"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved raffles")
    public ResponseEntity<List<Raffle>> getAllRaffles(
            @Parameter(description = "Filter by status (optional)")
            @RequestParam(required = false) String status) {
        
        List<Raffle> raffles;
        if ("active".equalsIgnoreCase(status)) {
            raffles = raffleService.getActiveRaffles();
        } else if ("preview".equalsIgnoreCase(status)) {
            raffles = raffleService.getPreviewRaffles();
        } else {
            raffles = raffleService.getAllRaffles();
        }
        
        return ResponseEntity.ok(raffles);
    }
}