package com.careernex.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.careernex.dto.QuizSubmissionRequest;
import com.careernex.dto.QuizSubmissionResponse;
import com.careernex.dto.QuizQuestionResponse;
import com.careernex.service.QuizService;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // Get all questions or filter by difficulty
    @GetMapping("/questions")
    public List<QuizQuestionResponse> getQuestions(
            @RequestParam Long roleId,
            @RequestParam(required = false) String difficulty) {

        if (difficulty == null || difficulty.isBlank()) {
            return quizService.getQuestionsByRole(roleId);
        }

        return quizService.getQuestionsByRoleAndDifficulty(
                roleId, difficulty);
    }
    
    @PostMapping("/submit")
    public QuizSubmissionResponse submitQuiz(
            @RequestParam Long roleId,
            @RequestBody QuizSubmissionRequest request) {

        return quizService.evaluateQuiz(roleId, request);
    }
}