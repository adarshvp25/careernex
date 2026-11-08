package com.careernex.dto;

import java.time.LocalDateTime;

public class AssessmentResponse {

    private Long id;
    private String targetRole;
    private Integer readinessScore;
    private Integer totalQuestions;
    private Integer attemptedQuestions;
    private Integer correctAnswers;
    private Integer incorrectAnswers;
    private LocalDateTime createdAt;

    public AssessmentResponse() {
    }

    public AssessmentResponse(
            Long id,
            String targetRole,
            Integer readinessScore,
            Integer totalQuestions,
            Integer attemptedQuestions,
            Integer correctAnswers,
            Integer incorrectAnswers,
            LocalDateTime createdAt) {

        this.id = id;
        this.targetRole = targetRole;
        this.readinessScore = readinessScore;
        this.totalQuestions = totalQuestions;
        this.attemptedQuestions = attemptedQuestions;
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = incorrectAnswers;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public Integer getReadinessScore() {
        return readinessScore;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public Integer getAttemptedQuestions() {
        return attemptedQuestions;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public Integer getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public void setReadinessScore(Integer readinessScore) {
        this.readinessScore = readinessScore;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public void setAttemptedQuestions(Integer attemptedQuestions) {
        this.attemptedQuestions = attemptedQuestions;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setIncorrectAnswers(Integer incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}