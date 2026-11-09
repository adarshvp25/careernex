
package com.careernex.dto;

import java.time.LocalDateTime;

public class UserSavedProjectDTO {

    private Long id;
    private Long projectId;
    private String projectTitle;
    private String skillName;
    private String description;
    private String difficulty;
    private String techStack;
    private String learningOutcome;
    private String status;
    private LocalDateTime addedAt;

    public UserSavedProjectDTO(Long id, Long projectId,
            String projectTitle, String skillName,
            String description, String difficulty,
            String techStack, String learningOutcome,
            String status, LocalDateTime addedAt) {

        this.id = id;
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.skillName = skillName;
        this.description = description;
        this.difficulty = difficulty;
        this.techStack = techStack;
        this.learningOutcome = learningOutcome;
        this.status = status;
        this.addedAt = addedAt;
    }

    public Long getId() { return id; }
    public Long getProjectId() { return projectId; }
    public String getProjectTitle() { return projectTitle; }
    public String getSkillName() { return skillName; }
    public String getDescription() { return description; }
    public String getDifficulty() { return difficulty; }
    public String getTechStack() { return techStack; }
    public String getLearningOutcome() { return learningOutcome; }
    public String getStatus() { return status; }
    public LocalDateTime getAddedAt() { return addedAt; }
}
