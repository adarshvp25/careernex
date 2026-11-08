
package com.careernex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.careernex.entity.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByUserId(Long userId);

    boolean existsByFileNameAndUserId(String fileName, Long userId);
}
