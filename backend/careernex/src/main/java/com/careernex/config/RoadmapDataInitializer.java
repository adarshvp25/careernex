
package com.careernex.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.careernex.entity.CareerRole;
import com.careernex.entity.RoadmapStep;
import com.careernex.repository.CareerRoleRepository;
import com.careernex.repository.RoadmapStepRepository;

@Component
public class RoadmapDataInitializer implements CommandLineRunner {

    private final CareerRoleRepository careerRoleRepository;
    private final RoadmapStepRepository roadmapStepRepository;

    public RoadmapDataInitializer(
            CareerRoleRepository careerRoleRepository,
            RoadmapStepRepository roadmapStepRepository) {

        this.careerRoleRepository = careerRoleRepository;
        this.roadmapStepRepository = roadmapStepRepository;
    }

    @Override
    public void run(String... args) {

        CareerRole role = careerRoleRepository.findAll()
                .stream()
                .filter(r -> r.getRoleCode().equals("JAVA_BACKEND"))
                .findFirst()
                .orElse(null);

        if (role == null) {
            return;
        }

        if (!roadmapStepRepository
                .findByCareerRoleIdOrderBySequenceNumberAsc(role.getId())
                .isEmpty()) {
            return;
        }

        addStep(role, 1, "Java", 21,
                "Learn Java syntax, OOP, collections and exception handling.",
                "Build a console-based student management application.");

        addStep(role, 2, "Spring Framework", 7,
                "Understand dependency injection, IoC and Spring beans.",
                "Create a small Spring application using dependency injection.");

        addStep(role, 3, "Spring Boot", 14,
                "Learn Spring Boot configuration, controllers and services.",
                "Build a basic Spring Boot application.");

        addStep(role, 4, "REST API", 7,
                "Understand HTTP methods, status codes and REST principles.",
                "Create CRUD REST endpoints for a student application.");

        addStep(role, 5, "MySQL", 10,
                "Learn SQL queries, joins, constraints and database design.",
                "Design a database for an employee management system.");

        addStep(role, 6, "Hibernate", 7,
                "Understand ORM, entities and Hibernate relationships.",
                "Map Java entities to MySQL tables.");

        addStep(role, 7, "Spring Data JPA", 7,
                "Learn repositories, derived queries and JPA operations.",
                "Implement database operations using JpaRepository.");

        addStep(role, 8, "Git", 3,
                "Learn version control, commits, branches and merging.",
                "Upload a Java project to GitHub using Git.");

        addStep(role, 9, "Maven", 3,
                "Understand dependencies, plugins and Maven build lifecycle.",
                "Create and build a Maven-based Spring Boot project.");
    }

    private void addStep(
            CareerRole role,
            int sequence,
            String skill,
            int days,
            String objective,
            String task) {

        RoadmapStep step = new RoadmapStep();

        step.setCareerRole(role);
        step.setSequenceNumber(sequence);
        step.setSkillName(skill);
        step.setEstimatedDays(days);
        step.setLearningObjective(objective);
        step.setPracticalTask(task);

        roadmapStepRepository.save(step);
    }
}
