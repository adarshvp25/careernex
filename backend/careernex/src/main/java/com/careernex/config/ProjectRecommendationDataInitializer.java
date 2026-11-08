package com.careernex.config;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import com.careernex.repository.ProjectRecommendationRepository;
import com.careernex.entity.ProjectRecommendation;

@Component
public class ProjectRecommendationDataInitializer implements CommandLineRunner {
		private final ProjectRecommendationRepository projectRecommendationRepository;
	
		public ProjectRecommendationDataInitializer(ProjectRecommendationRepository projectRecommendationRepository) {
			this.projectRecommendationRepository = projectRecommendationRepository;
		}
	
		@Override
		public void run(String... args) throws Exception {

		    if (projectRecommendationRepository.count() > 0) {
		        return;
		    }
		    
		    addRecommendation(
		            "Java",
		            "Student Management System",
		            "Create an application to manage student records, courses and grades.",
		            "BEGINNER",
		            "Java, OOP, Collections",
		            "Practise Java fundamentals, classes, objects and collection handling."
		    );

		    addRecommendation(
		            "Spring Framework",
		            "Dependency Injection Demo",
		            "Build an application demonstrating dependency injection and Spring Beans.",
		            "INTERMEDIATE",
		            "Java, Spring Framework",
		            "Understand IoC, dependency injection and Spring Beans."
		    );

		    addRecommendation(
		            "Spring Boot",
		            "Employee Management REST API",
		            "Develop REST endpoints to create, retrieve, update and delete employee records.",
		            "INTERMEDIATE",
		            "Java, Spring Boot, REST API",
		            "Learn Spring Boot application development and REST API creation."
		    );

		    addRecommendation(
		            "REST API",
		            "Task Management API",
		            "Create APIs for managing tasks, deadlines and task completion status.",
		            "INTERMEDIATE",
		            "Java, Spring Boot, REST API",
		            "Practise HTTP methods, status codes and API design."
		    );

		    addRecommendation(
		            "MySQL",
		            "Library Management Database",
		            "Design a relational database for books, members and book transactions.",
		            "BEGINNER",
		            "MySQL, SQL",
		            "Learn database design, joins, relationships and SQL queries."
		    );

		    addRecommendation(
		            "Hibernate",
		            "Product Inventory System",
		            "Build an application that manages products using Hibernate ORM.",
		            "INTERMEDIATE",
		            "Java, Hibernate, MySQL",
		            "Understand ORM, entity mapping and database relationships."
		    );

		    addRecommendation(
		            "Spring Data JPA",
		            "Online Bookstore Backend",
		            "Develop a backend for managing books, customers and orders using JPA repositories.",
		            "INTERMEDIATE",
		            "Java, Spring Boot, Spring Data JPA, MySQL",
		            "Practise repository interfaces, derived queries and CRUD operations."
		    );

		    addRecommendation(
		            "Maven",
		            "Modular Java Application",
		            "Create a Java application with multiple modules and external dependencies.",
		            "BEGINNER",
		            "Java, Maven",
		            "Understand pom.xml, dependencies and the Maven build lifecycle."
		    );

		    addRecommendation(
		            "Git",
		            "Collaborative Project Repository",
		            "Manage a sample application using branches, commits and merges.",
		            "BEGINNER",
		            "Git, GitHub",
		            "Practise version control and collaborative development."
		    );

		}
		
//		Recommendation Method
		private void addRecommendation(
		        String skillName,
		        String projectTitle,
		        String description,
		        String difficulty,
		        String techStack,
		        String learningOutcome) {

		    ProjectRecommendation project = new ProjectRecommendation();

		    project.setSkillName(skillName);
		    project.setProjectTitle(projectTitle);
		    project.setDescription(description);
		    project.setDifficulty(difficulty);
		    project.setTechStack(techStack);
		    project.setLearningOutcome(learningOutcome);

		    projectRecommendationRepository.save(project);
		}
}