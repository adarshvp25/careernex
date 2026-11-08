package com.careernex.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.careernex.entity.CareerRole;
import com.careernex.entity.QuizQuestion;
import com.careernex.repository.CareerRoleRepository;
import com.careernex.repository.QuizQuestionRepository;

@Component
public class QuizQuestionDataInitializer implements CommandLineRunner {

    private final CareerRoleRepository careerRoleRepository;
    private final QuizQuestionRepository quizQuestionRepository;

    public QuizQuestionDataInitializer(
            CareerRoleRepository careerRoleRepository,
            QuizQuestionRepository quizQuestionRepository) {

        this.careerRoleRepository = careerRoleRepository;
        this.quizQuestionRepository = quizQuestionRepository;
    }

    @Override
    public void run(String... args) {

        CareerRole role = careerRoleRepository.findAll()
                .stream()
                .filter(r -> "JAVA_BACKEND".equals(r.getRoleCode()))
                .findFirst()
                .orElse(null);

        if (role == null) {
            return;
        }

        // Avoid inserting duplicate questions on every restart
        if (!quizQuestionRepository
                .findByCareerRoleId(role.getId()).isEmpty()) {
            return;
        }

        List<QuizQuestion> questions = List.of(

        	createQuestion(role,
        			"Java",
        			"Which keyword is used to inherit a class in Java?",
        			"implements", "extends", "inherits", "super",
        			"B",
        			"The extends keyword is used for class inheritance.",
        			"EASY"),

            createQuestion(role,
            		"Java",
            		"Which of the following is not a primitive data type in Java?",
            		"int", "char", "boolean", "String",
            		"D", "String is a class, not a primitive data type.",
            		"EASY"),

            createQuestion(role,
            		"Java",
                "Which concept allows a class to have multiple methods with the same name but different parameters?",
                "Method overriding", "Method overloading",
                "Encapsulation", "Abstraction",
                "B", "Method overloading allows methods to share a name with different parameter lists.",
                "EASY"),

            createQuestion(role,"Java",
                "Which collection does not allow duplicate elements?",
                "ArrayList", "LinkedList", "HashSet", "Vector",
                "C", "HashSet stores unique elements.",
                "EASY"),

            createQuestion(role,"Java",
                "Which block is generally used to execute code whether an exception occurs or not?",
                "try", "catch", "throw", "finally",
                "D", "The finally block generally executes whether an exception occurs or not.",
                "MEDIUM"),

            createQuestion(role,"Spring Boot",
                "Which annotation is used to define a REST controller in Spring?",
                "@Controller", "@Service",
                "@RestController", "@Repository",
                "C", "@RestController combines @Controller and @ResponseBody.",
                "EASY"),

            createQuestion(role,"Spring Boot",
                "Which annotation is commonly used to map HTTP GET requests in Spring Boot?",
                "@PostMapping", "@GetMapping",
                "@PutMapping", "@DeleteMapping",
                "B", "@GetMapping maps HTTP GET requests to controller methods.",
                "EASY"),

            createQuestion(role,"Spring Data JPA",
                "Which interface is commonly used by Spring Data JPA repositories?",
                "List", "JpaRepository",
                "Runnable", "Serializable",
                "B", "JpaRepository provides CRUD operations and additional JPA functionality.",
                "MEDIUM"),

            createQuestion(role,"MySQL",
                "Which SQL command retrieves records from a database table?",
                "INSERT", "UPDATE", "SELECT", "DELETE",
                "C", "SELECT retrieves records from database tables.",
                "EASY"),

            createQuestion(role,"Spring Framework",
                "What is the primary purpose of dependency injection in Spring?",
                "To create database tables",
                "To manage and provide object dependencies",
                "To compile Java code",
                "To handle HTTP requests only",
                "B", "Dependency injection allows Spring to provide the dependencies an object requires.",
                "MEDIUM")
        );

        quizQuestionRepository.saveAll(questions);

        System.out.println("10 Java Backend quiz questions inserted.");
    }

    private QuizQuestion createQuestion(
            CareerRole role,
            String skillName,
            String questionText,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctOption,
            String explanation,
            String difficulty) {

        QuizQuestion question = new QuizQuestion();

        question.setCareerRole(role);
        question.setSkillName(skillName);
        question.setQuestionText(questionText);
        question.setOptionA(optionA);
        question.setOptionB(optionB);
        question.setOptionC(optionC);
        question.setOptionD(optionD);
        question.setCorrectOption(correctOption);
        question.setExplanation(explanation);
        question.setDifficulty(difficulty);

        return question;
    }
}