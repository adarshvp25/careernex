
package com.careernex.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.careernex.entity.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "target_role", nullable = false)
    private String targetRole;

    @Column(name = "readiness_score")
    private Integer readinessScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Automatically set creation time
    @PrePersist
    public void setCreationTime() {
        createdAt = LocalDateTime.now();
    }
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Default constructor required by JPA
    public Assessment() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public Integer getReadinessScore() {
        return readinessScore;
    }

    public void setReadinessScore(Integer readinessScore) {
        this.readinessScore = readinessScore;
    }
    
    @Column(nullable = true)
    private Integer totalQuestions;

    @Column(nullable = true)
    private Integer attemptedQuestions;

    @Column(nullable = true)
    private Integer correctAnswers;

    @Column(nullable = true)
    private Integer incorrectAnswers;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getAttemptedQuestions() {
        return attemptedQuestions;
    }

    public void setAttemptedQuestions(Integer attemptedQuestions) {
        this.attemptedQuestions = attemptedQuestions;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Integer getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(Integer incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }
}
