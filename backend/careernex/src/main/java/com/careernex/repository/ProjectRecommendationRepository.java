package com.careernex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.careernex.entity.ProjectRecommendation;

public interface ProjectRecommendationRepository
        extends JpaRepository<ProjectRecommendation, Long> {
}