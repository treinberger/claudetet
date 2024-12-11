package com.allianz.raffle.service;

import com.allianz.raffle.model.Raffle;
import com.allianz.raffle.model.PrizeTier;
import com.allianz.raffle.model.AnswerOption;
import com.allianz.raffle.model.enums.RaffleStatus;
import com.allianz.raffle.repository.RaffleRepository;
import com.allianz.raffle.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaffleService {

    private final RaffleRepository raffleRepository;

    @Autowired
    public RaffleService(RaffleRepository raffleRepository) {
        this.raffleRepository = raffleRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Raffle> getRaffleById(Long id) {
        return raffleRepository.findById(id)
                .map(this::updateRaffleStatus);
    }

    @Transactional
    public Raffle createRaffle(Raffle raffle) {
        validateRaffle(raffle);
        raffle.setStatus(RaffleStatus.DRAFT);
        
        if (raffle.getPrizeTiers() != null) {
            raffle.getPrizeTiers().forEach(prizeTier -> prizeTier.setRaffle(raffle));
        }
        
        return raffleRepository.save(raffle);
    }

    private void validateRaffle(Raffle raffle) {
        if (raffle.getName() == null || raffle.getName().trim().isEmpty()) {
            throw new ValidationException("Raffle name is required");
        }

        if (raffle.getStartDate() == null) {
            throw new ValidationException("Start date is required");
        }

        if (raffle.getEndDate() == null) {
            throw new ValidationException("End date is required");
        }

        if (raffle.getPreviewDate() == null) {
            throw new ValidationException("Preview date is required");
        }

        if (raffle.getQuestion() == null || raffle.getQuestion().trim().isEmpty()) {
            throw new ValidationException("Question is required");
        }

        validateAnswerOptions(raffle.getAnswerOptions());

        if (raffle.getEndDate().isBefore(raffle.getStartDate())) {
            throw new ValidationException("End date must be after start date");
        }

        if (raffle.getStartDate().isBefore(raffle.getPreviewDate())) {
            throw new ValidationException("Start date must be after preview date");
        }

        if (raffle.getPrizeTiers() == null || raffle.getPrizeTiers().isEmpty()) {
            throw new ValidationException("At least one prize tier is required");
        }
        
        validatePrizeTiers(raffle.getPrizeTiers());
    }

    private void validateAnswerOptions(List<AnswerOption> answerOptions) {
        if (answerOptions == null || answerOptions.size() < 2) {
            throw new ValidationException("At least two answer options are required");
        }

        long correctCount = answerOptions.stream()
                .filter(AnswerOption::isCorrect)
                .count();

        if (correctCount != 1) {
            throw new ValidationException("Exactly one answer must be marked as correct");
        }

        for (AnswerOption option : answerOptions) {
            if (option.getText() == null || option.getText().trim().isEmpty()) {
                throw new ValidationException("Answer option text cannot be empty");
            }
        }
    }

    private void validatePrizeTiers(List<PrizeTier> prizeTiers) {
        for (PrizeTier prizeTier : prizeTiers) {
            if (prizeTier.getTier() == null || prizeTier.getTier() < 0) {
                throw new ValidationException("Prize tier must have a valid tier number");
            }
            if (prizeTier.getDescription() == null || prizeTier.getDescription().trim().isEmpty()) {
                throw new ValidationException("Prize tier must have a description");
            }
            if (prizeTier.getQuantity() == null || prizeTier.getQuantity() < 1) {
                throw new ValidationException("Prize tier must have a valid quantity (minimum 1)");
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Raffle> getAllRaffles() {
        List<Raffle> raffles = raffleRepository.findAll();
        return raffles.stream()
                .map(this::updateRaffleStatus)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Raffle> getActiveRaffles() {
        LocalDateTime now = LocalDateTime.now();
        return raffleRepository.findByStartDateBeforeAndEndDateAfter(now, now)
                .stream()
                .filter(raffle -> raffle.getStatus() == RaffleStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Raffle> getPreviewRaffles() {
        LocalDateTime now = LocalDateTime.now();
        return raffleRepository.findAll().stream()
                .filter(raffle -> raffle.getPreviewDate().isBefore(now) 
                        && raffle.getStartDate().isAfter(now)
                        && raffle.getStatus() == RaffleStatus.PREVIEW)
                .collect(Collectors.toList());
    }

    private Raffle updateRaffleStatus(Raffle raffle) {
        LocalDateTime now = LocalDateTime.now();
        
        if (raffle.getStatus() == RaffleStatus.DRAWN) {
            return raffle;
        }

        if (now.isBefore(raffle.getPreviewDate())) {
            raffle.setStatus(RaffleStatus.DRAFT);
        } else if (now.isBefore(raffle.getStartDate())) {
            raffle.setStatus(RaffleStatus.PREVIEW);
        } else if (now.isBefore(raffle.getEndDate())) {
            raffle.setStatus(RaffleStatus.ACTIVE);
        } else {
            raffle.setStatus(RaffleStatus.ENDED);
        }

        return raffleRepository.save(raffle);
    }
}