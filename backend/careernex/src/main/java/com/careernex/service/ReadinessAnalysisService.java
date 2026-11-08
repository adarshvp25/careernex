package com.careernex.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.careernex.entity.Assessment;

@Service
public class ReadinessAnalysisService {
	private final SkillGapAnalysisService skillGapAnalysisService;
	private final AssessmentService assessmentService;
	private final ResumeService resumeService;
	private final RecommendationService recommendationService;
	
	public ReadinessAnalysisService(
	        SkillGapAnalysisService skillGapAnalysisService,
	        AssessmentService assessmentService,
	        ResumeService resumeService, RecommendationService recommendationService) {

	    this.skillGapAnalysisService = skillGapAnalysisService;
	    this.assessmentService = assessmentService;
	    this.resumeService = resumeService;
	    this.recommendationService = recommendationService;
	}
	

	public Map<String, Object> calculateReadiness(
	        Long resumeId,
	        Long roleId,
	        Long assessmentId) throws IOException {

	    // Get resume skill-gap analysis
	    Map<String, Object> skillAnalysis =
	            skillGapAnalysisService.analyzeSkills(
	                    roleId,
	                    resumeService.extractResumeSkills(resumeId));

	    // Get the selected assessment
	    Assessment assessment =
	            assessmentService.getAssessmentById(assessmentId);

	    if (assessment == null) {
	        throw new ResponseStatusException(
	                HttpStatus.NOT_FOUND,
	                "Assessment not found");
	    }

	    // Calculate skill coverage and quiz score
	    double skillCoverage =
	            ((Number) skillAnalysis.get("skillCoverage")).doubleValue();

	    double quizScore = assessment.getReadinessScore();
	   
	 // Generate personalized learning recommendations
	    List<Map<String, String>> recommendations =
	            recommendationService.generateRecommendations(
	                    skillAnalysis);

	    // Calculate overall readiness using 60:40 weighting
	    double overallReadiness =
	            (skillCoverage * 0.60) + (quizScore * 0.40);

	    overallReadiness =
	            Math.round(overallReadiness * 100.0) / 100.0;

	    return Map.ofEntries(

	            Map.entry("skillCoverage", skillCoverage),
	            Map.entry("quizScore", quizScore),
	            Map.entry("overallReadiness", overallReadiness),
	            Map.entry("weightage", "60% Skills + 40% Quiz"),

	            Map.entry("matchedSkills",
	                    skillAnalysis.get("matchedSkills")),

	            Map.entry("missingSkills",
	                    skillAnalysis.get("missingSkills")),

	            Map.entry("missingMandatorySkills",
	                    skillAnalysis.get("missingMandatorySkills")),

	            Map.entry("missingOptionalSkills",
	                    skillAnalysis.get("missingOptionalSkills")),

	            Map.entry("totalRequiredSkills",
	                    skillAnalysis.get("totalRequiredSkills")),

	            Map.entry("matchedCount",
	                    skillAnalysis.get("matchedCount")),

	            Map.entry("missingCount",
	                    skillAnalysis.get("missingCount")),
	            
	            Map.entry("recommendations", recommendations)
	    );
	}
}
