package com.allianz.raffle.controller;

import com.allianz.raffle.model.Raffle;
import com.allianz.raffle.model.PrizeTier;
import com.allianz.raffle.model.enums.RaffleStatus;
import com.allianz.raffle.service.RaffleService;
import com.allianz.raffle.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RaffleControllerTest {

    @Mock
    private RaffleService raffleService;

    private RaffleController raffleController;

    @BeforeEach
    void setUp() {
        raffleController = new RaffleController(raffleService);
    }

    @Test
    void createRaffle_ValidRaffle_ShouldReturn201() {
        // Arrange
        Raffle raffle = createValidRaffle(LocalDateTime.now());
        when(raffleService.createRaffle(any(Raffle.class))).thenReturn(raffle);

        // Act
        ResponseEntity<?> response = raffleController.createRaffle(raffle);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(raffle, response.getBody());
        verify(raffleService).createRaffle(raffle);
    }

    @Test
    void createRaffle_ValidationError_ShouldReturn400() {
        // Arrange
        Raffle raffle = new Raffle(); // Invalid raffle
        when(raffleService.createRaffle(any(Raffle.class)))
            .thenThrow(new ValidationException("Validation error"));

        // Act
        ResponseEntity<?> response = raffleController.createRaffle(raffle);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Validation error", errorResponse.get("error"));
    }

    @Test
    void getAllRaffles_NoStatus_ShouldReturnAllRaffles() {
        // Arrange
        List<Raffle> expectedRaffles = Arrays.asList(new Raffle(), new Raffle());
        when(raffleService.getAllRaffles()).thenReturn(expectedRaffles);

        // Act
        ResponseEntity<List<Raffle>> response = raffleController.getAllRaffles(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRaffles, response.getBody());
        verify(raffleService).getAllRaffles();
    }

    @Test
    void getAllRaffles_ActiveStatus_ShouldReturnActiveRaffles() {
        // Arrange
        List<Raffle> expectedRaffles = Arrays.asList(new Raffle(), new Raffle());
        when(raffleService.getActiveRaffles()).thenReturn(expectedRaffles);

        // Act
        ResponseEntity<List<Raffle>> response = raffleController.getAllRaffles("active");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRaffles, response.getBody());
        verify(raffleService).getActiveRaffles();
    }

    private Raffle createValidRaffle(LocalDateTime now) {
        Raffle raffle = new Raffle();
        raffle.setName("Test Raffle");
        raffle.setPreviewDate(now.plusDays(1));
        raffle.setStartDate(now.plusDays(2));
        raffle.setEndDate(now.plusDays(3));
        raffle.setQuestion("Test Question?");
        raffle.setAnswerOptions(Arrays.asList("Option 1", "Option 2"));
        
        PrizeTier prizeTier = new PrizeTier();
        prizeTier.setTier(1);
        prizeTier.setDescription("First Prize");
        prizeTier.setQuantity(1);
        raffle.setPrizeTiers(Arrays.asList(prizeTier));

        return raffle;
    }
}