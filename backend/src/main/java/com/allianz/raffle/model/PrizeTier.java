// File: /backend/src/main/java/com/allianz/raffle/model/PrizeTier.java

package com.allianz.raffle.model;

import javax.persistence.*;

@Entity
@Table(name = "prize_tiers")
public class PrizeTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer tier;
    private String description;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "raffle_id")
    private Raffle raffle;

    // Constructors
    public PrizeTier() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Raffle getRaffle() {
        return raffle;
    }

    public void setRaffle(Raffle raffle) {
        this.raffle = raffle;
    }
}