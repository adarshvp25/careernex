
package com.careernex.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.careernex.entity.UserRoadmapProgress;

public interface UserRoadmapProgressRepository
        extends JpaRepository<UserRoadmapProgress, Long> {

    List<UserRoadmapProgress> findByUserId(Long userId);

    Optional<UserRoadmapProgress> findByUserIdAndRoadmapStepId(
            Long userId, Long roadmapStepId);
}
