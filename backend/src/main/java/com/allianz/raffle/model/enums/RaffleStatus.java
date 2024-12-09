// File: /backend/src/main/java/com/allianz/raffle/model/enums/RaffleStatus.java

package com.allianz.raffle.model.enums;

public enum RaffleStatus {
    DRAFT,       // Initial state when created
    PREVIEW,     // Visible but not yet accepting entries
    ACTIVE,      // Accepting entries
    ENDED,       // No longer accepting entries but not drawn
    DRAWN        // Winner has been selected
}