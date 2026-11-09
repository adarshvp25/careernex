
package com.careernex.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.careernex.entity.UserSavedProject;

public interface UserSavedProjectRepository
        extends JpaRepository<UserSavedProject, Long> {

    List<UserSavedProject> findByUser_Id(Long userId);
    
    boolean existsByUser_IdAndProject_Id(Long userId, Long projectId);
    
    Optional<UserSavedProject> findByIdAndUser_Id(Long id, Long userId);
}
