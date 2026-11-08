package com.careernex.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.careernex.service.ReadinessAnalysisService;

@RestController
@RequestMapping("/api/readiness")
public class ReadinessAnalysisController {

    private final ReadinessAnalysisService readinessAnalysisService;

    public ReadinessAnalysisController(
            ReadinessAnalysisService readinessAnalysisService) {

        this.readinessAnalysisService = readinessAnalysisService;
    }

    @GetMapping("/analyze")
    public Map<String, Object> analyzeReadiness(
            @RequestParam Long resumeId,
            @RequestParam Long roleId,
            @RequestParam Long assessmentId) throws IOException {

        return readinessAnalysisService.calculateReadiness(
                resumeId, roleId, assessmentId);
    }
}