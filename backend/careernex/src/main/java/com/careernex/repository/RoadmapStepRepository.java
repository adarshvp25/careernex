
package com.careernex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.careernex.entity.RoadmapStep;

public interface RoadmapStepRepository
        extends JpaRepository<RoadmapStep, Long> {

    List<RoadmapStep> findByCareerRoleIdOrderBySequenceNumberAsc(
            Long careerRoleId);
}
