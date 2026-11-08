
package com.careernex.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.careernex.dto.AssessmentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.careernex.entity.Assessment;
import com.careernex.service.AssessmentService;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    // Create assessment
    @PostMapping
    public ResponseEntity<Assessment> createAssessment(
            @RequestBody Assessment assessment) {

        Assessment savedAssessment =
                assessmentService.saveAssessment(assessment);

        return new ResponseEntity<>(savedAssessment, HttpStatus.CREATED);
    }

    // Get logged-in user's assessments
    @GetMapping
    public ResponseEntity<List<AssessmentResponse>> getAllAssessments() {

        List<AssessmentResponse> assessments =
                assessmentService.getAllAssessments()
                        .stream()
                        .map(assessment -> new AssessmentResponse(
                                assessment.getId(),
                                assessment.getTargetRole(),
                                assessment.getReadinessScore(),
                                assessment.getTotalQuestions(),
                                assessment.getAttemptedQuestions(),
                                assessment.getCorrectAnswers(),
                                assessment.getIncorrectAnswers(),
                                assessment.getCreatedAt()
                        ))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(assessments);
    }

    // Get assessment by ID
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentResponse> getAssessmentById(
            @PathVariable Long id) {

        Assessment assessment =
                assessmentService.getAssessmentById(id);

        if (assessment == null) {
            return ResponseEntity.notFound().build();
        }

        AssessmentResponse response = new AssessmentResponse(
                assessment.getId(),
                assessment.getTargetRole(),
                assessment.getReadinessScore(),
                assessment.getTotalQuestions(),
                assessment.getAttemptedQuestions(),
                assessment.getCorrectAnswers(),
                assessment.getIncorrectAnswers(),
                assessment.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    // Delete assessment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(
            @PathVariable Long id) {

        Assessment assessment =
                assessmentService.getAssessmentById(id);

        if (assessment == null) {
            return ResponseEntity.notFound().build();
        }

        assessmentService.deleteAssessment(id);

        return ResponseEntity.noContent().build();
    }
}
