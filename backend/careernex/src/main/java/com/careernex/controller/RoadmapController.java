
package com.careernex.controller;

import java.util.List;
import java.util.Map;
import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.careernex.entity.RoadmapStep;
import com.careernex.repository.RoadmapStepRepository;
import com.careernex.service.RoadmapService;

@RestController
@RequestMapping("/api/roadmap")
public class RoadmapController {

	private final RoadmapStepRepository roadmapStepRepository;
	private final RoadmapService roadmapService;

	public RoadmapController(
	        RoadmapStepRepository roadmapStepRepository,
	        RoadmapService roadmapService) {

	    this.roadmapStepRepository = roadmapStepRepository;
	    this.roadmapService = roadmapService;
	}

    @GetMapping
    public List<Map<String, Object>> getRoadmap(
            @RequestParam Long roleId) {

        List<RoadmapStep> steps =
                roadmapStepRepository
                .findByCareerRoleIdOrderBySequenceNumberAsc(roleId);

        return steps.stream()
                .map(step -> Map.<String, Object>of(
                        "id", step.getId(),
                        "sequenceNumber", step.getSequenceNumber(),
                        "skillName", step.getSkillName(),
                        "estimatedDays", step.getEstimatedDays(),
                        "learningObjective",
                        step.getLearningObjective(),
                        "practicalTask",
                        step.getPracticalTask()
                ))
                .toList();
    }
    
    @GetMapping("/personalized")
    public List<Map<String, Object>> getPersonalizedRoadmap(
            @RequestParam Long resumeId,
            @RequestParam Long roleId) throws IOException {

        return roadmapService.getPersonalizedRoadmap(resumeId, roleId)
                .stream()
                .map(step -> Map.<String, Object>of(
                        "id", step.getId(),
                        "sequenceNumber", step.getSequenceNumber(),
                        "skillName", step.getSkillName(),
                        "estimatedDays", step.getEstimatedDays(),
                        "learningObjective", step.getLearningObjective(),
                        "practicalTask", step.getPracticalTask()
                ))
                .toList();
    }
}
