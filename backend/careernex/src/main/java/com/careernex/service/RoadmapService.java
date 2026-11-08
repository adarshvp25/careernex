
package com.careernex.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.careernex.entity.RoadmapStep;
import com.careernex.repository.RoadmapStepRepository;

@Service
public class RoadmapService {

    private final RoadmapStepRepository roadmapStepRepository;
    private final ResumeService resumeService;

    public RoadmapService(
            RoadmapStepRepository roadmapStepRepository,
            ResumeService resumeService) {

        this.roadmapStepRepository = roadmapStepRepository;
        this.resumeService = resumeService;
    }

    private String normalizeSkill(String skill) {

        String normalized = skill.trim()
                .toLowerCase(Locale.ROOT);

        return switch (normalized) {
            case "html" -> "html5";
            case "css" -> "css3";
            case "js" -> "javascript";
            case "react.js", "reactjs" -> "react";
            case "nodejs" -> "node.js";
            case "springboot" -> "spring boot";
            case "restful api", "restful apis" -> "rest api";
            default -> normalized;
        };
    }

    public List<RoadmapStep> getPersonalizedRoadmap(
            Long resumeId,
            Long roleId) throws IOException {

        // Extract skills from the uploaded resume
        List<String> detectedSkills =
                resumeService.extractResumeSkills(resumeId);

        // Normalize detected skills
        Set<String> detected = detectedSkills.stream()
                .map(this::normalizeSkill)
                .collect(Collectors.toSet());

        // Retrieve the complete roadmap for the selected role
        List<RoadmapStep> roadmap =
                roadmapStepRepository
                .findByCareerRoleIdOrderBySequenceNumberAsc(roleId);

        // Return only the skills missing from the resume
        return roadmap.stream()
                .filter(step -> !detected.contains(
                        normalizeSkill(step.getSkillName())))
                .toList();
    }
}
