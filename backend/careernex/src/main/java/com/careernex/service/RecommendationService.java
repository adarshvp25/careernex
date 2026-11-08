package com.careernex.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    // Store learning recommendations for individual skills
    private final Map<String, String> recommendations =
            new LinkedHashMap<>();

    public RecommendationService() {

        recommendations.put("Java",
                "Learn Java fundamentals, OOP, Collections and Exception Handling.");

        recommendations.put("Spring Framework",
                "Study Dependency Injection, Inversion of Control and Spring Beans.");

        recommendations.put("Spring Boot",
                "Build REST APIs using Spring Boot and understand auto-configuration.");

        recommendations.put("REST API",
                "Learn HTTP methods, status codes, request handling and API design.");

        recommendations.put("MySQL",
                "Practise SQL queries, joins, relationships and database design.");

        recommendations.put("Hibernate",
                "Learn ORM concepts, entity mapping and Hibernate relationships.");

        recommendations.put("Spring Data JPA",
                "Practise repository interfaces, derived queries and CRUD operations.");

        recommendations.put("Maven",
                "Learn Maven dependencies, pom.xml and project build lifecycle.");

        recommendations.put("Git",
                "Practise version control, commits, branches and merging.");
    }

    // Generate recommendations based on missing skills
    public List<Map<String, String>> generateRecommendations(
            Map<String, Object> skillAnalysis) {

        List<String> missingSkills =
                (List<String>) skillAnalysis.get("missingSkills");

        List<String> mandatorySkills =
                (List<String>) skillAnalysis.get("missingMandatorySkills");

        List<Map<String, String>> result = new ArrayList<>();

        for (String skill : missingSkills) {

            String recommendation = recommendations.getOrDefault(
                    skill,
                    "Study the fundamentals of " + skill +
                    " and practise through a practical project."
            );

            String priority = mandatorySkills.contains(skill)
                    ? "HIGH"
                    : "MEDIUM";

            Map<String, String> item = new LinkedHashMap<>();

            item.put("skillName", skill);
            item.put("priority", priority);
            item.put("recommendation", recommendation);

            result.add(item);
        }

        return result;
    }
}