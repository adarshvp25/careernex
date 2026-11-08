package com.careernex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.careernex.entity.QuizQuestion;

public interface QuizQuestionRepository
        extends JpaRepository<QuizQuestion, Long> {

    List<QuizQuestion> findByCareerRoleId(Long careerRoleId);

    List<QuizQuestion> findByCareerRoleIdAndDifficulty(
            Long careerRoleId, String difficulty);
}