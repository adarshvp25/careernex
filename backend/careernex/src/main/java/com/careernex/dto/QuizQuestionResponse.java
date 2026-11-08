package com.careernex.dto;

public class QuizQuestionResponse {

    private Long id;
    private String questionText;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String difficulty;
    private String skillName;

    public QuizQuestionResponse() {
    }

    public QuizQuestionResponse(
            Long id,
            String questionText,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String difficulty,
            String skillName) {

        this.id = id;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.difficulty = difficulty;
        this.skillName = skillName;
    }

    public Long getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
}