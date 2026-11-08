
package com.careernex.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.careernex.service.AssessmentService;
import com.careernex.service.SkillGapAnalysisService;
import com.careernex.service.UserRoadmapProgressService;
import com.careernex.service.RecommendationService;
import com.careernex.service.ProjectRecommendationService;
import com.careernex.service.ResumeService;
import com.careernex.repository.CareerRoleRepository;
import com.careernex.entity.Assessment;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final AssessmentService assessmentService;
    private final SkillGapAnalysisService skillGapAnalysisService;
    private final UserRoadmapProgressService userRoadmapProgressService;
    private final RecommendationService recommendationService;
    private final ProjectRecommendationService projectRecommendationService;
    private final CareerRoleRepository careerRoleRepository;
    private final ResumeService resumeService;

    public DashboardController(
            AssessmentService assessmentService,
            SkillGapAnalysisService skillGapAnalysisService,
            UserRoadmapProgressService userRoadmapProgressService,
            RecommendationService recommendationService,
            ProjectRecommendationService projectRecommendationService,
            CareerRoleRepository careerRoleRepository,
            ResumeService resumeService) {

        this.assessmentService = assessmentService;
        this.skillGapAnalysisService = skillGapAnalysisService;
        this.userRoadmapProgressService = userRoadmapProgressService;
        this.recommendationService = recommendationService;
        this.projectRecommendationService = projectRecommendationService;
        this.careerRoleRepository = careerRoleRepository;
        this.resumeService = resumeService;
    }

    @GetMapping
    public Map<String, Object> getDashboard(
            @RequestParam Long resumeId,
            @RequestParam Long roleId,
            @RequestParam Long assessmentId) throws IOException {

        List<String> detectedSkills =
                resumeService.extractResumeSkills(resumeId);

        Map<String, Object> skillAnalysis =
                skillGapAnalysisService.analyzeSkills(
                        roleId, detectedSkills);

        Assessment assessment =
                assessmentService.getAssessmentById(assessmentId);

        if (assessment == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Assessment not found"
            );
        }
        
        
        double skillCoverage =
                ((Number) skillAnalysis.get("skillCoverage")).doubleValue();

        double quizScore = assessment.getReadinessScore();

        double overallReadiness =
                Math.round((skillCoverage * 0.60 + quizScore * 0.40) * 100.0) / 100.0;
        
        Map<String, Object> roadmapProgress =
                userRoadmapProgressService.getRoadmapProgressSummary(
                        resumeId, roleId);
        
        List<Map<String, String>> learningRecommendations =
                recommendationService.generateRecommendations(skillAnalysis);
        
        List<com.careernex.entity.ProjectRecommendation> projectRecommendations =
                projectRecommendationService.getPersonalizedRecommendations(
                        resumeId, roleId);
        
        var careerRole = careerRoleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Career role not found"
                ));
        
        return Map.of(
                "skillAnalysis", skillAnalysis,
                "quizScore", quizScore,
                "skillCoverage", skillCoverage,
                "overallReadiness", overallReadiness,
                "roadmapProgress", roadmapProgress,
                "learningRecommendations", learningRecommendations,
                "projectRecommendations", projectRecommendations,
                "roleName", careerRole.getRoleName(),
                "roleCode", careerRole.getRoleCode()
        );
    }
}
