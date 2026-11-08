
package com.careernex.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "roadmap_steps",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"career_role_id", "sequence_number"}
        )
    }
)
public class RoadmapStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_role_id", nullable = false)
    private CareerRole careerRole;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(name = "sequence_number", nullable = false)
    private Integer sequenceNumber;

    @Column(name = "estimated_days", nullable = false)
    private Integer estimatedDays;

    @Column(length = 1000)
    private String learningObjective;

    @Column(length = 1000)
    private String practicalTask;

    public RoadmapStep() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CareerRole getCareerRole() {
        return careerRole;
    }

    public void setCareerRole(CareerRole careerRole) {
        this.careerRole = careerRole;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Integer getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(Integer estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public String getLearningObjective() {
        return learningObjective;
    }

    public void setLearningObjective(String learningObjective) {
        this.learningObjective = learningObjective;
    }

    public String getPracticalTask() {
        return practicalTask;
    }

    public void setPracticalTask(String practicalTask) {
        this.practicalTask = practicalTask;
    }
}
