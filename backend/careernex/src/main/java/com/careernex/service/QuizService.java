package com.careernex.service;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.careernex.dto.QuizSubmissionRequest;
import com.careernex.dto.QuizSubmissionResponse;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.careernex.dto.QuizQuestionResponse;
import com.careernex.entity.QuizQuestion;
import com.careernex.repository.QuizQuestionRepository;
import com.careernex.repository.CareerRoleRepository;
import com.careernex.service.AssessmentService;
import com.careernex.entity.Assessment;
import com.careernex.entity.CareerRole;

@Service
public class QuizService {

    private final QuizQuestionRepository quizQuestionRepository;
    private final AssessmentService assessmentService;
    private final CareerRoleRepository careerRoleRepository;

    public QuizService(
            QuizQuestionRepository quizQuestionRepository,
            AssessmentService assessmentService,
            CareerRoleRepository careerRoleRepository) {

        this.quizQuestionRepository = quizQuestionRepository;
        this.assessmentService = assessmentService;
        this.careerRoleRepository = careerRoleRepository;
    }

    // Retrieve quiz questions for a selected career role
    public List<QuizQuestionResponse> getQuestionsByRole(Long roleId) {

        List<QuizQuestion> questions =
                quizQuestionRepository.findByCareerRoleId(roleId);

        return questions.stream()
                .map(question -> new QuizQuestionResponse(
                        question.getId(),
                        question.getQuestionText(),
                        question.getOptionA(),
                        question.getOptionB(),
                        question.getOptionC(),
                        question.getOptionD(),
                        question.getDifficulty(),
                        question.getSkillName()
                ))
                .toList();
    }

    // Retrieve questions filtered by difficulty
    public List<QuizQuestionResponse> getQuestionsByRoleAndDifficulty(
            Long roleId, String difficulty) {

        List<QuizQuestion> questions =
                quizQuestionRepository
                        .findByCareerRoleIdAndDifficulty(
                                roleId, difficulty.toUpperCase());

        return questions.stream()
                .map(question -> new QuizQuestionResponse(
                        question.getId(),
                        question.getQuestionText(),
                        question.getOptionA(),
                        question.getOptionB(),
                        question.getOptionC(),
                        question.getOptionD(),
                        question.getDifficulty(),
                        question.getSkillName()
                ))
                .toList();
    }
    
    
    public QuizSubmissionResponse evaluateQuiz(
            Long roleId,
            QuizSubmissionRequest request) {

        List<QuizQuestion> questions =
                quizQuestionRepository.findByCareerRoleId(roleId);

        if (questions.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No quiz questions found for this role");
        }

        if (request.getAnswers() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Answers are required");
        }

        Set<Long> submittedQuestionIds = new HashSet<>();

        for (QuizSubmissionRequest.Answer answer : request.getAnswers()) {

            if (answer.getQuestionId() == null ||
                    !submittedQuestionIds.add(answer.getQuestionId())) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid or duplicate question ID");
            }
        }

        int correct = 0;
        int attempted = 0;

        List<QuizSubmissionResponse.AnswerResult> results =
                new ArrayList<>();

        for (QuizQuestion question : questions) {

            QuizSubmissionRequest.Answer submittedAnswer =
                    request.getAnswers().stream()
                            .filter(answer ->
                                    question.getId().equals(
                                            answer.getQuestionId()))
                            .findFirst()
                            .orElse(null);

            if (submittedAnswer == null) {
                continue;
            }

            String selectedOption = submittedAnswer
                    .getSelectedOption();

            if (selectedOption == null ||
                    !selectedOption.matches("(?i)[A-D]")) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid answer option");
            }

            attempted++;

            boolean isCorrect = question.getCorrectOption()
                    .equalsIgnoreCase(selectedOption);

            if (isCorrect) {
                correct++;
            }

            QuizSubmissionResponse.AnswerResult result =
                    new QuizSubmissionResponse.AnswerResult();

            result.setQuestionId(question.getId());
            result.setQuestionText(question.getQuestionText());
            result.setSkillName(question.getSkillName());
            result.setSelectedOption(selectedOption.toUpperCase());
            result.setCorrectOption(question.getCorrectOption());
            result.setCorrect(isCorrect);
            result.setExplanation(question.getExplanation());

            results.add(result);
        }

        if (submittedQuestionIds.size() != attempted) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One or more submitted questions do not belong to this role");
        }

        int totalQuestions = questions.size();

        double percentage = (correct * 100.0) / totalQuestions;

        QuizSubmissionResponse response =
                new QuizSubmissionResponse();

        response.setTotalQuestions(totalQuestions);
        response.setAttemptedQuestions(attempted);
        response.setCorrectAnswers(correct);
        response.setIncorrectAnswers(attempted - correct);
        response.setScorePercentage(
                Math.round(percentage * 100.0) / 100.0);
        response.setResults(results);

	     // Find the selected career role
	     CareerRole careerRole = careerRoleRepository
	             .findById(roleId)
	             .orElseThrow(() ->
	                     new ResponseStatusException(
	                             HttpStatus.NOT_FOUND,
	                             "Career role not found"));
	
	     // Create assessment record
	     Assessment assessment = new Assessment();
	
	     assessment.setTargetRole(careerRole.getRoleCode());
	     assessment.setReadinessScore((int) Math.round(response.getScorePercentage()));
	
	     assessment.setTotalQuestions(totalQuestions);
	     assessment.setAttemptedQuestions(attempted);
	     assessment.setCorrectAnswers(correct);
	     assessment.setIncorrectAnswers(attempted - correct);
	
	     // Save assessment for the authenticated user
	     Assessment savedAssessment = assessmentService.saveAssessment(assessment);

	     response.setAssessmentId(savedAssessment.getId());

	     return response;
    }
}