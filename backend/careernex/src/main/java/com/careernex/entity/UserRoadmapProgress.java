
package com.careernex.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(
    name = "user_roadmap_progress",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"user_id", "roadmap_step_id"}
        )
    }
)
public class UserRoadmapProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_step_id", nullable = false)
    private RoadmapStep roadmapStep;

    @Column(nullable = false)
    private String status = "NOT_STARTED";

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    public UserRoadmapProgress() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RoadmapStep getRoadmapStep() {
        return roadmapStep;
    }

    public void setRoadmapStep(RoadmapStep roadmapStep) {
        this.roadmapStep = roadmapStep;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
