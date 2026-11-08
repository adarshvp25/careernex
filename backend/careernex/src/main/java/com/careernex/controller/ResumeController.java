
package com.careernex.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.careernex.entity.Resume;
import com.careernex.service.ResumeService;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    // Upload resume
    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file) throws IOException {

        Resume resume = resumeService.uploadResume(file);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                    "message", "Resume uploaded successfully",
                    "id", resume.getId(),
                    "fileName", resume.getFileName(),
                    "uploadedAt", resume.getUploadedAt()
                )
        );
    }

    // Get logged-in user's resumes
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getMyResumes() {

        List<Map<String, Object>> resumes = resumeService.getMyResumes()
                .stream()
                .map(resume -> Map.<String, Object>of(
                        "id", resume.getId(),
                        "fileName", resume.getFileName(),
                        "uploadedAt", resume.getUploadedAt()
                ))
                .toList();

        return ResponseEntity.ok(resumes);
    }
    

	@GetMapping("/{id}/text")
	public ResponseEntity<?> extractResumeText(@PathVariable Long id)
	        throws IOException {
	
	    String extractedText = resumeService.extractResumeText(id);
	
	    return ResponseEntity.ok(
	            Map.of("extractedText", extractedText)
	    );
	}
	

	@GetMapping("/{id}/skills")
	public ResponseEntity<?> extractResumeSkills(@PathVariable Long id)
	        throws IOException {
	
	    List<String> skills = resumeService.extractResumeSkills(id);
	
	    return ResponseEntity.ok(
	            Map.of("detectedSkills", skills)
	    );
	}


}
