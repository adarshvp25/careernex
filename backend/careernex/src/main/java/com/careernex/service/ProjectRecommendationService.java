package com.careernex.service;

import java.util.List;
import java.util.Map;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.careernex.entity.ProjectRecommendation;
import com.careernex.repository.ProjectRecommendationRepository;
import com.careernex.service.SkillGapAnalysisService;
import com.careernex.service.ResumeService;

@Service
public class ProjectRecommendationService {

    private final ProjectRecommendationRepository projectRecommendationRepository;
    private final SkillGapAnalysisService skillGapAnalysisService;
    private final ResumeService resumeService;

    public ProjectRecommendationService(ProjectRecommendationRepository projectRecommendationRepository,
    		SkillGapAnalysisService skillGapAnalysisService,
            ResumeService resumeService) {

        this.projectRecommendationRepository = projectRecommendationRepository;
        this.skillGapAnalysisService = skillGapAnalysisService;
        this.resumeService = resumeService;
    }
    
    public List<ProjectRecommendation> getRecommendationsBySkills(
            List<String> missingSkills) {

        List<ProjectRecommendation> recommendations =
                projectRecommendationRepository.findAll();

        return recommendations.stream()
                .filter(project ->
                        missingSkills.contains(project.getSkillName()))
                .toList();
    }
    
    public List<ProjectRecommendation> getPersonalizedRecommendations(
            Long resumeId, Long roleId) throws IOException {

        List<String> detectedSkills =
                resumeService.extractResumeSkills(resumeId);

        Map<String, Object> skillAnalysis =
                skillGapAnalysisService.analyzeSkills(roleId, detectedSkills);

        @SuppressWarnings("unchecked")
        List<String> missingSkills =
                (List<String>) skillAnalysis.get("missingSkills");

        return getRecommendationsBySkills(missingSkills);
    }

}