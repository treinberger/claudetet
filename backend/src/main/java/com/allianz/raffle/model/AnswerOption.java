package com.allianz.raffle.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "answer_options")
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "raffle_id")
    @JsonIgnore  // Prevent infinite recursion during serialization
    private Raffle raffle;

    // Default constructor
    public AnswerOption() {}

    // Factory method
    public static AnswerOption of(String text, boolean isCorrect) {
        AnswerOption ao = new AnswerOption();
        ao.setText(text);
        ao.setCorrect(isCorrect);
        return ao;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Raffle getRaffle() {
        return raffle;
    }

    public void setRaffle(Raffle raffle) {
        this.raffle = raffle;
    }
}