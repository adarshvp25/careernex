
package com.careernex.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.careernex.entity.User;
import com.careernex.entity.RoadmapStep;
import com.careernex.entity.UserRoadmapProgress;

import com.careernex.repository.UserRepository;
import com.careernex.repository.RoadmapStepRepository;
import com.careernex.repository.UserRoadmapProgressRepository;

@Service
public class UserRoadmapProgressService {

    private final UserRoadmapProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final RoadmapStepRepository roadmapStepRepository;
    private final RoadmapService roadmapService;

    public UserRoadmapProgressService(
            UserRoadmapProgressRepository progressRepository,
            UserRepository userRepository,
            RoadmapStepRepository roadmapStepRepository,
            RoadmapService roadmapService) {

        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
        this.roadmapStepRepository = roadmapStepRepository;
        this.roadmapService = roadmapService;
    }

    // Get currently authenticated user
    private User getCurrentUser() {

        UserDetails userDetails = (UserDetails)
                SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    // Retrieve all progress records of current user
    public List<UserRoadmapProgress> getUserProgress() {

        User user = getCurrentUser();

        return progressRepository.findByUserId(user.getId());
    }

    // Update learning progress
    public UserRoadmapProgress updateProgress(
            Long roadmapStepId,
            Long resumeId,
            Long roleId,
            String status) throws IOException {

        if (!List.of(
                "NOT_STARTED",
                "IN_PROGRESS",
                "COMPLETED"
        ).contains(status)) {

            throw new IllegalArgumentException(
                    "Invalid progress status");
        }

        User user = getCurrentUser();

        RoadmapStep step = roadmapStepRepository
                .findById(roadmapStepId)
                .orElseThrow(() ->
                	new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                			"Roadmap step not found"));
        
        List<RoadmapStep> personalizedRoadmap =
                roadmapService.getPersonalizedRoadmap(resumeId, roleId);

        boolean isValidStep = personalizedRoadmap.stream()
                .anyMatch(roadmapStep ->
                        roadmapStep.getId().equals(roadmapStepId));

        if (!isValidStep) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "This step does not belong to your personalized roadmap");
        }

        UserRoadmapProgress progress = progressRepository
                .findByUserIdAndRoadmapStepId(
                        user.getId(), roadmapStepId)
                .orElseGet(UserRoadmapProgress::new);

        progress.setUser(user);
        progress.setRoadmapStep(step);
        progress.setStatus(status);

        if ("IN_PROGRESS".equals(status)
                && progress.getStartedAt() == null) {

            progress.setStartedAt(LocalDateTime.now());
        }

        if ("COMPLETED".equals(status)) {

            if (progress.getStartedAt() == null) {
                progress.setStartedAt(LocalDateTime.now());
            }

            progress.setCompletedAt(LocalDateTime.now());
        }

        if ("NOT_STARTED".equals(status)) {

            progress.setStartedAt(null);
            progress.setCompletedAt(null);
        }

        return progressRepository.save(progress);
    }

    // Calculate overall personalized roadmap progress
    public Map<String, Object> getRoadmapProgressSummary(
            Long resumeId,
            Long roleId) throws java.io.IOException {

        User user = getCurrentUser();

        // Get personalized roadmap
        List<RoadmapStep> roadmap =
                roadmapService.getPersonalizedRoadmap(
                        resumeId, roleId);

        // Get user's saved progress
        List<UserRoadmapProgress> progressList =
                progressRepository.findByUserId(user.getId());

        // Collect roadmap step IDs into a Set
        Set<Long> roadmapStepIds = roadmap.stream()
                .map(RoadmapStep::getId)
                .collect(Collectors.toSet());

        // Match progress records with personalized roadmap
        Map<Long, String> progressMap = progressList.stream()
                .filter(p -> roadmapStepIds.contains(
                        p.getRoadmapStep().getId()))
                .collect(Collectors.toMap(
                        p -> p.getRoadmapStep().getId(),
                        UserRoadmapProgress::getStatus,
                        (existing, replacement) -> replacement
                ));

        int completed = 0;
        int inProgress = 0;
        int notStarted = 0;
        int completedDays = 0;

        // Calculate progress statistics
        for (RoadmapStep step : roadmap) {

            String status = progressMap.get(step.getId());

            if ("COMPLETED".equals(status)) {

                completed++;

                completedDays += step.getEstimatedDays();
            }
            else if ("IN_PROGRESS".equals(status)) {

                inProgress++;
            }
            else {

                notStarted++;
            }
        }

        int totalSteps = roadmap.size();

        // Calculate completion percentage
        double completionPercentage = totalSteps == 0
                ? 0
                : (completed * 100.0) / totalSteps;

        // Return progress summary
        return Map.of(
                "totalSteps", totalSteps,
                "completedSteps", completed,
                "inProgressSteps", inProgress,
                "notStartedSteps", notStarted,
                "completionPercentage",
                Math.round(completionPercentage * 100.0) / 100.0,
                "completedLearningDays", completedDays
        );
    }
}