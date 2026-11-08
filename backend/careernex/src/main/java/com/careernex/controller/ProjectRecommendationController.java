package com.careernex.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.careernex.entity.ProjectRecommendation;
import com.careernex.service.ProjectRecommendationService;

@RestController
@RequestMapping("/api/projects")
public class ProjectRecommendationController {
    private final ProjectRecommendationService projectRecommendationService;

    public ProjectRecommendationController(ProjectRecommendationService projectRecommendationService) {
        this.projectRecommendationService = projectRecommendationService;
    }
    
    @GetMapping("/recommendations")
    public List<ProjectRecommendation> getPersonalizedRecommendations(
            @RequestParam Long resumeId,
            @RequestParam Long roleId) throws IOException {

        return projectRecommendationService
                .getPersonalizedRecommendations(resumeId, roleId);
    }

}