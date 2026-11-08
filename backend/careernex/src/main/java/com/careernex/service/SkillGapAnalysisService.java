
package com.careernex.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.careernex.entity.RoleSkill;
import com.careernex.repository.RoleSkillRepository;

@Service
public class SkillGapAnalysisService {

    private final RoleSkillRepository roleSkillRepository;

    public SkillGapAnalysisService(
            RoleSkillRepository roleSkillRepository) {
        this.roleSkillRepository = roleSkillRepository;
    }

    private String normalizeSkill(String skill) {

        String normalized = skill.trim()
                .toLowerCase(Locale.ROOT);

        return switch (normalized) {
            case "html" -> "html5";
            case "css" -> "css3";
            case "js" -> "javascript";
            case "react.js", "reactjs" -> "react";
            case "nodejs" -> "node.js";
            case "springboot" -> "spring boot";
            case "restful api", "restful apis" -> "rest api";
            default -> normalized;
        };
    }

    public Map<String, Object> analyzeSkills(
            Long roleId,
            List<String> detectedSkills) {

        List<RoleSkill> requiredSkills =
                roleSkillRepository.findByCareerRoleId(roleId);

        Set<String> detected = detectedSkills.stream()
                .map(this::normalizeSkill)
                .collect(Collectors.toSet());

        List<String> matchedSkills = requiredSkills.stream()
                .filter(skill -> detected.contains(
                        normalizeSkill(skill.getSkillName())))
                .map(RoleSkill::getSkillName)
                .toList();

        List<String> missingSkills = requiredSkills.stream()
                .filter(skill -> !detected.contains(
                        normalizeSkill(skill.getSkillName())))
                .map(RoleSkill::getSkillName)
                .toList();

        List<String> missingMandatorySkills = requiredSkills.stream()
                .filter(skill -> Boolean.TRUE.equals(skill.getMandatory()))
                .filter(skill -> !detected.contains(
                        normalizeSkill(skill.getSkillName())))
                .map(RoleSkill::getSkillName)
                .toList();

        List<String> missingOptionalSkills = requiredSkills.stream()
                .filter(skill -> !Boolean.TRUE.equals(skill.getMandatory()))
                .filter(skill -> !detected.contains(
                        normalizeSkill(skill.getSkillName())))
                .map(RoleSkill::getSkillName)
                .toList();

        int totalWeight = requiredSkills.stream()
                .mapToInt(skill ->
                        Boolean.TRUE.equals(skill.getMandatory()) ? 2 : 1)
                .sum();

        int achievedWeight = requiredSkills.stream()
                .filter(skill -> detected.contains(
                        normalizeSkill(skill.getSkillName())))
                .mapToInt(skill ->
                        Boolean.TRUE.equals(skill.getMandatory()) ? 2 : 1)
                .sum();

        double coverage = totalWeight == 0
                ? 0
                : (achievedWeight * 100.0) / totalWeight;

        return Map.of(
                "totalRequiredSkills", requiredSkills.size(),
                "matchedCount", matchedSkills.size(),
                "missingCount", missingSkills.size(),
                "matchedSkills", matchedSkills,
                "missingSkills", missingSkills,
                "missingMandatorySkills", missingMandatorySkills,
                "missingOptionalSkills", missingOptionalSkills,
                "totalWeight", totalWeight,
                "achievedWeight", achievedWeight,
                "skillCoverage",
                Math.round(coverage * 100.0) / 100.0
        );
    }
}
