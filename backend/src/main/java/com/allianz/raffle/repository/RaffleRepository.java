// File: /backend/src/main/java/com/allianz/raffle/repository/RaffleRepository.java

package com.allianz.raffle.repository;

import com.allianz.raffle.model.Raffle;
import com.allianz.raffle.model.enums.RaffleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RaffleRepository extends JpaRepository<Raffle, Long> {
    
    List<Raffle> findByStatus(RaffleStatus status);
    
    List<Raffle> findByStartDateBeforeAndEndDateAfter(LocalDateTime now, LocalDateTime now2);
    
    List<Raffle> findByEndDateBeforeAndStatusNot(LocalDateTime now, RaffleStatus status);
    
    // TODO: Add more custom query methods as needed
}