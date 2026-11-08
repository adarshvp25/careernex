
package com.careernex.controller;

import java.util.List;
import java.util.Map;
import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.careernex.service.ResumeService;
import com.careernex.service.SkillGapAnalysisService;

@RestController
@RequestMapping("/api/skill-gap")
public class SkillGapAnalysisController {

    private final ResumeService resumeService;
    private final SkillGapAnalysisService skillGapAnalysisService;

    public SkillGapAnalysisController(
            ResumeService resumeService,
            SkillGapAnalysisService skillGapAnalysisService) {

        this.resumeService = resumeService;
        this.skillGapAnalysisService = skillGapAnalysisService;
    }

    @GetMapping("/analyze")
    public Map<String, Object> analyzeSkillGap(
            @RequestParam Long resumeId,
            @RequestParam Long roleId) throws IOException {

        List<String> detectedSkills =
                resumeService.extractResumeSkills(resumeId);

        return skillGapAnalysisService.analyzeSkills(
                roleId,
                detectedSkills
        );
    }
}
