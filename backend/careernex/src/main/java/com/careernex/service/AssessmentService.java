
package com.careernex.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.careernex.entity.Assessment;
import com.careernex.entity.User;
import com.careernex.repository.AssessmentRepository;
import com.careernex.repository.UserRepository;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final UserRepository userRepository;

    public AssessmentService(
            AssessmentRepository assessmentRepository,
            UserRepository userRepository) {

        this.assessmentRepository = assessmentRepository;
        this.userRepository = userRepository;
    }

    // Get the currently logged-in user
    private User getCurrentUser() {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String email = ((UserDetails) principal).getUsername();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Save assessment for the logged-in user
    public Assessment saveAssessment(Assessment assessment) {

        User currentUser = getCurrentUser();

        assessment.setUser(currentUser);

        return assessmentRepository.save(assessment);
    }

    // Get assessments belonging to the logged-in user
    public List<Assessment> getAllAssessments() {

        User currentUser = getCurrentUser();

        return assessmentRepository.findByUserId(currentUser.getId());
    }

    // Get assessment by ID, but only if it belongs to the logged-in user
    public Assessment getAssessmentById(Long id) {

        User currentUser = getCurrentUser();

        return assessmentRepository.findById(id)
                .filter(assessment ->
                        assessment.getUser().getId().equals(currentUser.getId()))
                .orElse(null);
    }

    // Delete assessment only if it belongs to the logged-in user
    public void deleteAssessment(Long id) {

        Assessment assessment = getAssessmentById(id);

        if (assessment != null) {
            assessmentRepository.delete(assessment);
        }
    }
}
