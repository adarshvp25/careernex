
package com.careernex.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import com.careernex.entity.UserRoadmapProgress;
import com.careernex.service.UserRoadmapProgressService;

@RestController
@RequestMapping("/api/roadmap/progress")
public class UserRoadmapProgressController {

    private final UserRoadmapProgressService progressService;

    public UserRoadmapProgressController(
            UserRoadmapProgressService progressService) {

        this.progressService = progressService;
    }

    @GetMapping
    public List<Map<String, Object>> getUserProgress() {

        return progressService.getUserProgress()
                .stream()
                .map(progress -> Map.<String, Object>of(
                        "id", progress.getId(),
                        "roadmapStepId",
                        progress.getRoadmapStep().getId(),
                        "skillName",
                        progress.getRoadmapStep().getSkillName(),
                        "status", progress.getStatus(),
                        "startedAt",
                        progress.getStartedAt() == null
                                ? ""
                                : progress.getStartedAt().toString(),
                        "completedAt",
                        progress.getCompletedAt() == null
                                ? ""
                                : progress.getCompletedAt().toString()
                ))
                .toList();
    }
    
    @GetMapping("/summary")
    public Map<String, Object> getProgressSummary(
            @RequestParam Long resumeId,
            @RequestParam Long roleId) throws IOException {

        return progressService.getRoadmapProgressSummary(
                resumeId, roleId);
    }

    @PutMapping("/{roadmapStepId}")
    public Map<String, Object> updateProgress(
            @PathVariable Long roadmapStepId,
            @RequestBody ProgressRequest request) throws IOException {

        UserRoadmapProgress progress =
        		progressService.updateProgress(
        		        roadmapStepId,
        		        request.getResumeId(),
        		        request.getRoleId(),
        		        request.getStatus()
        		);

        return Map.of(
                "message", "Progress updated successfully",
                "roadmapStepId", roadmapStepId,
                "status", progress.getStatus()
        );
    }

    public static class ProgressRequest {

        private String status;
        private Long resumeId;
        private Long roleId;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        
        public Long getResumeId() {
            return resumeId;
        }

        public void setResumeId(Long resumeId) {
            this.resumeId = resumeId;
        }

        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }
    }
}
