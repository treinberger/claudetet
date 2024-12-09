// File: /backend/src/main/java/com/allianz/raffle/model/ApointsConfig.java

package com.allianz.raffle.model;

import javax.persistence.Embeddable;

@Embeddable
public class ApointsConfig {
    private Integer costPerChance;
    private Integer maxPurchases;

    // Constructors
    public ApointsConfig() {}

    public ApointsConfig(Integer costPerChance, Integer maxPurchases) {
        this.costPerChance = costPerChance;
        this.maxPurchases = maxPurchases;
    }

    // Getters and Setters
    public Integer getCostPerChance() {
        return costPerChance;
    }

    public void setCostPerChance(Integer costPerChance) {
        this.costPerChance = costPerChance;
    }

    public Integer getMaxPurchases() {
        return maxPurchases;
    }

    public void setMaxPurchases(Integer maxPurchases) {
        this.maxPurchases = maxPurchases;
    }
}