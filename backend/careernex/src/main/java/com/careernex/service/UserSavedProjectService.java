
package com.careernex.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.careernex.dto.UserSavedProjectDTO;
import com.careernex.entity.ProjectRecommendation;
import com.careernex.entity.User;
import com.careernex.entity.UserSavedProject;
import com.careernex.repository.ProjectRecommendationRepository;
import com.careernex.repository.UserRepository;
import com.careernex.repository.UserSavedProjectRepository;

@Service
public class UserSavedProjectService {

    private final UserSavedProjectRepository savedProjectRepository;
    private final UserRepository userRepository;
    private final ProjectRecommendationRepository projectRepository;

    public UserSavedProjectService(
            UserSavedProjectRepository savedProjectRepository,
            UserRepository userRepository,
            ProjectRecommendationRepository projectRepository) {

        this.savedProjectRepository = savedProjectRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public UserSavedProjectDTO saveProject(String email, Long projectId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProjectRecommendation project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (savedProjectRepository.existsByUser_IdAndProject_Id(
                user.getId(), projectId)) {
            throw new RuntimeException("Project already exists in your roadmap");
        }

        UserSavedProject savedProject = new UserSavedProject();
        savedProject.setUser(user);
        savedProject.setProject(project);
        savedProject.setStatus("NOT_STARTED");

        UserSavedProject saved = savedProjectRepository.save(savedProject);

        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<UserSavedProjectDTO> getSavedProjects(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return savedProjectRepository.findByUser_Id(user.getId())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    private UserSavedProjectDTO convertToDTO(UserSavedProject saved) {

        ProjectRecommendation project = saved.getProject();

        return new UserSavedProjectDTO(
                saved.getId(),
                project.getId(),
                project.getProjectTitle(),
                project.getSkillName(),
                project.getDescription(),
                project.getDifficulty(),
                project.getTechStack(),
                project.getLearningOutcome(),
                saved.getStatus(),
                saved.getAddedAt()
        );
    }
    

	@Transactional
	public void removeSavedProject(String email, Long savedProjectId) {
	
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	
	    UserSavedProject savedProject =
	            savedProjectRepository.findByIdAndUser_Id(
	                    savedProjectId, user.getId())
	            .orElseThrow(() ->
	                    new RuntimeException("Saved project not found"));
	
	    savedProjectRepository.delete(savedProject);
	}

}
