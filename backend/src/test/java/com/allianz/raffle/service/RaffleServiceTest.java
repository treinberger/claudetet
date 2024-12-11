package com.allianz.raffle.service;

import com.allianz.raffle.model.Raffle;
import com.allianz.raffle.model.AnswerOption;
import com.allianz.raffle.model.PrizeTier;
import com.allianz.raffle.model.enums.RaffleStatus;
import com.allianz.raffle.repository.RaffleRepository;
import com.allianz.raffle.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RaffleServiceTest {

    @Mock
    private RaffleRepository raffleRepository;

    private RaffleService raffleService;

    @BeforeEach
    void setUp() {
        raffleService = new RaffleService(raffleRepository);
    }

    @Test
    void createRaffle_ValidRaffle_ShouldSucceed() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Raffle raffle = createValidRaffle(now);
        when(raffleRepository.save(any(Raffle.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Raffle result = raffleService.createRaffle(raffle);

        // Assert
        assertEquals(RaffleStatus.DRAFT, result.getStatus());
        verify(raffleRepository).save(raffle);
    }

    @Test
    void createRaffle_NoName_ShouldThrowValidationException() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Raffle raffle = createValidRaffle(now);
        raffle.setName(null);
        
        // Act & Assert
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> raffleService.createRaffle(raffle)
        );
        assertEquals("Raffle name is required", exception.getMessage());
        verify(raffleRepository, never()).save(any());
    }

    @Test
    void createRaffle_InvalidDates_ShouldThrowValidationException() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Raffle raffle = createValidRaffle(now);
        raffle.setStartDate(now.plusDays(1));
        raffle.setPreviewDate(now.plusDays(2)); // Preview after start

        // Act & Assert
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> raffleService.createRaffle(raffle)
        );
        assertEquals("Start date must be after preview date", exception.getMessage());
        verify(raffleRepository, never()).save(any());
    }

    @Test
    void createRaffle_InsufficientAnswerOptions_ShouldThrowValidationException() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Raffle raffle = createValidRaffle(now);
        raffle.setAnswerOptions(List.of(AnswerOption.of("Option 1", false)));

        // Act & Assert
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> raffleService.createRaffle(raffle)
        );
        assertEquals("At least two answer options are required", exception.getMessage());
        verify(raffleRepository, never()).save(any());
    }

    @Test
    void createRaffle_NoPrizeTiers_ShouldThrowValidationException() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Raffle raffle = createValidRaffle(now);
        raffle.setPrizeTiers(new ArrayList<>());

        // Act & Assert
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> raffleService.createRaffle(raffle)
        );
        assertEquals("At least one prize tier is required", exception.getMessage());
        verify(raffleRepository, never()).save(any());
    }

    @Test
    void getAllRaffles_ShouldUpdateStatusBasedOnDates() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        
        Raffle activeRaffle = new Raffle();
        activeRaffle.setStartDate(now.minusDays(1));
        activeRaffle.setEndDate(now.plusDays(1));
        activeRaffle.setPreviewDate(now.minusDays(2));
        activeRaffle.setStatus(RaffleStatus.ACTIVE);

        Raffle endedRaffle = new Raffle();
        endedRaffle.setStartDate(now.minusDays(2));
        endedRaffle.setEndDate(now.minusDays(1));
        endedRaffle.setPreviewDate(now.minusDays(3));
        endedRaffle.setStatus(RaffleStatus.ACTIVE);

        when(raffleRepository.findAll()).thenReturn(Arrays.asList(activeRaffle, endedRaffle));
        when(raffleRepository.save(any(Raffle.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        List<Raffle> result = raffleService.getAllRaffles();

        // Assert
        assertEquals(2, result.size());
        assertEquals(RaffleStatus.ACTIVE, result.get(0).getStatus());
        assertEquals(RaffleStatus.ENDED, result.get(1).getStatus());
        verify(raffleRepository, times(2)).save(any(Raffle.class));
    }

    @Test
    void getActiveRaffles_ShouldOnlyReturnActiveRaffles() {
        // Arrange
        when(raffleRepository.findByStartDateBeforeAndEndDateAfter(any(), any()))
            .thenReturn(Arrays.asList(
                createRaffle(RaffleStatus.ACTIVE),
                createRaffle(RaffleStatus.ENDED)
            ));

        // Act
        List<Raffle> result = raffleService.getActiveRaffles();

        // Assert
        assertEquals(1, result.size());
        assertEquals(RaffleStatus.ACTIVE, result.get(0).getStatus());
    }

    @Test
    void getAllRaffles_ShouldNotUpdateDrawnRaffles() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        
        Raffle drawnRaffle = new Raffle();
        drawnRaffle.setStartDate(now.minusDays(2));
        drawnRaffle.setEndDate(now.minusDays(1));
        drawnRaffle.setPreviewDate(now.minusDays(3));
        drawnRaffle.setStatus(RaffleStatus.DRAWN);

        when(raffleRepository.findAll()).thenReturn(Arrays.asList(drawnRaffle));

        // Act
        List<Raffle> result = raffleService.getAllRaffles();

        // Assert
        assertEquals(1, result.size());
        assertEquals(RaffleStatus.DRAWN, result.get(0).getStatus());
        verify(raffleRepository, never()).save(any(Raffle.class));
    }

    private Raffle createValidRaffle(LocalDateTime now) {
        Raffle raffle = new Raffle();
        raffle.setName("Test Raffle");
        raffle.setPreviewDate(now.plusDays(1));
        raffle.setStartDate(now.plusDays(2));
        raffle.setEndDate(now.plusDays(3));
        raffle.setQuestion("Test Question?");
        raffle.setAnswerOptions(List.of(AnswerOption.of("Option 1", false), AnswerOption.of("Option 2", true), AnswerOption.of("Option 3", false)));
        
        PrizeTier prizeTier = new PrizeTier();
        prizeTier.setTier(1);
        prizeTier.setDescription("First Prize");
        prizeTier.setQuantity(1);
        raffle.setPrizeTiers(Arrays.asList(prizeTier));

        return raffle;
    }

    private Raffle createRaffle(RaffleStatus status) {
        Raffle raffle = new Raffle();
        raffle.setStatus(status);
        return raffle;
    }
}