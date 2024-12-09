// File: /backend/src/main/java/com/allianz/raffle/controller/RaffleController.java

package com.allianz.raffle.controller;

import com.allianz.raffle.model.Raffle;
import com.allianz.raffle.service.RaffleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raffles")
public class RaffleController {

    @Autowired
    private RaffleService raffleService;

    @GetMapping
    public ResponseEntity<List<Raffle>> getAllRaffles() {
        // TODO: Implement get all raffles
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Raffle> getRaffleById(@PathVariable Long id) {
        // TODO: Implement get raffle by id
        return null;
    }

    @PostMapping
    public ResponseEntity<Raffle> createRaffle(@RequestBody Raffle raffle) {
        // TODO: Implement create raffle
        return null;
    }

    // TODO: Add other endpoints for raffle management
}