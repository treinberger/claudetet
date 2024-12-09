package com.allianz.raffle.model;

import com.allianz.raffle.model.enums.RaffleStatus;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "raffles")
public class Raffle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String teaserImage;
    private String detailImage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime previewDate;
    private String question;

    @ElementCollection
    private List<String> answerOptions = new ArrayList<>();

    @OneToMany(mappedBy = "raffle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrizeTier> prizeTiers = new ArrayList<>();

    @Embedded
    private ApointsConfig apointsConfig;

    @Enumerated(EnumType.STRING)
    private RaffleStatus status;

    // Constructors
    public Raffle() {
        this.apointsConfig = new ApointsConfig();
    }

    // Helper method to manage bidirectional relationship
    public void addPrizeTier(PrizeTier prizeTier) {
        prizeTiers.add(prizeTier);
        prizeTier.setRaffle(this);
    }

    public void removePrizeTier(PrizeTier prizeTier) {
        prizeTiers.remove(prizeTier);
        prizeTier.setRaffle(null);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeaserImage() {
        return teaserImage;
    }

    public void setTeaserImage(String teaserImage) {
        this.teaserImage = teaserImage;
    }

    public String getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(String detailImage) {
        this.detailImage = detailImage;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getPreviewDate() {
        return previewDate;
    }

    public void setPreviewDate(LocalDateTime previewDate) {
        this.previewDate = previewDate;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<String> answerOptions) {
        this.answerOptions = answerOptions;
    }

    public List<PrizeTier> getPrizeTiers() {
        return prizeTiers;
    }

    public void setPrizeTiers(List<PrizeTier> prizeTiers) {
        // Clear existing prize tiers
        this.prizeTiers.clear();
        if (prizeTiers != null) {
            // Add each prize tier using the helper method to maintain the relationship
            prizeTiers.forEach(this::addPrizeTier);
        }
    }

    public ApointsConfig getApointsConfig() {
        return apointsConfig;
    }

    public void setApointsConfig(ApointsConfig apointsConfig) {
        this.apointsConfig = apointsConfig;
    }

    public RaffleStatus getStatus() {
        return status;
    }

    public void setStatus(RaffleStatus status) {
        this.status = status;
    }
}