package com.careernex.dto;

import java.util.List;

public class QuizSubmissionResponse {

    private int totalQuestions;
    private int attemptedQuestions;
    private int correctAnswers;
    private int incorrectAnswers;
    private double scorePercentage;
    private Long assessmentId;

    private List<AnswerResult> results;

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getAttemptedQuestions() {
        return attemptedQuestions;
    }

    public void setAttemptedQuestions(int attemptedQuestions) {
        this.attemptedQuestions = attemptedQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public double getScorePercentage() {
        return scorePercentage;
    }

    public void setScorePercentage(double scorePercentage) {
        this.scorePercentage = scorePercentage;
    }
    
    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public List<AnswerResult> getResults() {
        return results;
    }

    public void setResults(List<AnswerResult> results) {
        this.results = results;
    }

    public static class AnswerResult {

        private Long questionId;
        private String questionText;
        private String skillName;
        private String selectedOption;
        private String correctOption;
        private boolean correct;
        private String explanation;

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public String getQuestionText() {
            return questionText;
        }

        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }

        public String getSelectedOption() {
            return selectedOption;
        }

        public void setSelectedOption(String selectedOption) {
            this.selectedOption = selectedOption;
        }

        public String getCorrectOption() {
            return correctOption;
        }

        public void setCorrectOption(String correctOption) {
            this.correctOption = correctOption;
        }

        public boolean isCorrect() {
            return correct;
        }

        public void setCorrect(boolean correct) {
            this.correct = correct;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }
        
        public String getSkillName() {
            return skillName;
        }

        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }
    }
}