
package com.careernex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.careernex.entity.Assessment;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    List<Assessment> findByUserId(Long userId);

}
