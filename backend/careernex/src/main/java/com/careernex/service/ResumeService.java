
package com.careernex.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.careernex.entity.Resume;
import com.careernex.entity.User;
import com.careernex.repository.ResumeRepository;
import com.careernex.repository.UserRepository;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final PdfTextExtractionService pdfTextExtractionService;
    private final SkillExtractionService skillExtractionService;

    private final Path uploadDirectory =
            Paths.get("uploads", "resumes").toAbsolutePath().normalize();

    public ResumeService(
            ResumeRepository resumeRepository,
            UserRepository userRepository,
            PdfTextExtractionService pdfTextExtractionService,
            SkillExtractionService skillExtractionService) {

        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.pdfTextExtractionService = pdfTextExtractionService;
        this.skillExtractionService = skillExtractionService;
    }

    // Get currently authenticated user
    private User getCurrentUser() {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String email = ((UserDetails) principal).getUsername();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
    }

    // Upload resume
    public Resume uploadResume(MultipartFile file) throws IOException {

        // Check whether a file was provided
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please select a file"
            );
        }

        // Check file size (5 MB maximum)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new ResponseStatusException(
                    HttpStatus.PAYLOAD_TOO_LARGE,
                    "File size must not exceed 5 MB"
            );
        }

        // Check original filename
        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null ||
                !originalFileName.toLowerCase().endsWith(".pdf")) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only PDF files are allowed"
            );
        }

        // Check MIME type
        if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid PDF file type"
            );
        }

        // Verify actual PDF file signature
        byte[] fileHeader = new byte[5];

        try (InputStream inputStream = file.getInputStream()) {

            int bytesRead = inputStream.read(fileHeader);

            if (bytesRead != 5 ||
                    !"%PDF-".equals(
                            new String(fileHeader, StandardCharsets.US_ASCII))) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid PDF file content"
                );
            }
        }

        // Get authenticated user
        User currentUser = getCurrentUser();

        // Create upload directory if it does not exist
        Files.createDirectories(uploadDirectory);

        // Generate a unique filename
        String storedFileName = UUID.randomUUID() + ".pdf";

        Path destination = uploadDirectory
                .resolve(storedFileName)
                .normalize();

        // Prevent path traversal
        if (!destination.startsWith(uploadDirectory)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid file path"
            );
        }

        // Save file
        file.transferTo(destination);

        try {

            // Save resume metadata
            Resume resume = new Resume();

            resume.setFileName(originalFileName);
            resume.setFilePath(destination.toString());
            resume.setUser(currentUser);

            return resumeRepository.save(resume);

        } catch (RuntimeException exception) {

            // Delete uploaded file if database saving fails
            Files.deleteIfExists(destination);

            throw exception;
        }
    }

    // Get current user's resumes
    public List<Resume> getMyResumes() {

        User currentUser = getCurrentUser();

        return resumeRepository.findByUserId(currentUser.getId());
    }

    // Extract resume text
    public String extractResumeText(Long resumeId) throws IOException {

        User currentUser = getCurrentUser();

        Resume resume = resumeRepository.findById(resumeId)
                .filter(r -> r.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Resume not found"
                ));

        return pdfTextExtractionService.extractText(
                resume.getFilePath()
        );
    }

    // Extract resume skills
    public List<String> extractResumeSkills(Long resumeId) throws IOException {

        User currentUser = getCurrentUser();

        Resume resume = resumeRepository.findById(resumeId)
                .filter(r -> r.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Resume not found"
                ));

        String resumeText = pdfTextExtractionService.extractText(
                resume.getFilePath()
        );

        return skillExtractionService.extractSkills(resumeText);
    }
}
