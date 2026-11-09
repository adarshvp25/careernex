
package com.careernex.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.careernex.dto.UserSavedProjectDTO;
import com.careernex.service.UserSavedProjectService;

@RestController
@RequestMapping("/api/projects")
public class UserSavedProjectController {

    private final UserSavedProjectService savedProjectService;

    public UserSavedProjectController(
            UserSavedProjectService savedProjectService) {
        this.savedProjectService = savedProjectService;
    }

    @PostMapping("/{projectId}/save")
    public UserSavedProjectDTO saveProject(
            @PathVariable Long projectId,
            Authentication authentication) {

        return savedProjectService.saveProject(
                authentication.getName(), projectId);
    }

    @GetMapping("/saved")
    public List<UserSavedProjectDTO> getSavedProjects(
            Authentication authentication) {

        return savedProjectService.getSavedProjects(
                authentication.getName());
    }
    

	@DeleteMapping("/saved/{savedProjectId}")
	public void removeSavedProject(
	        @PathVariable Long savedProjectId,
	        Authentication authentication) {
	
	    savedProjectService.removeSavedProject(
	            authentication.getName(), savedProjectId);
	}

}
