
package com.careernex.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.careernex.entity.CareerRole;
import com.careernex.entity.RoleSkill;
import com.careernex.repository.CareerRoleRepository;
import com.careernex.repository.RoleSkillRepository;

@Component
public class CareerRoleDataInitializer implements CommandLineRunner {

    private final CareerRoleRepository careerRoleRepository;
    private final RoleSkillRepository roleSkillRepository;

    public CareerRoleDataInitializer(
            CareerRoleRepository careerRoleRepository,
            RoleSkillRepository roleSkillRepository) {

        this.careerRoleRepository = careerRoleRepository;
        this.roleSkillRepository = roleSkillRepository;
    }

    @Override
    public void run(String... args) {

        createRole(
            "Java Backend Developer",
            "JAVA_BACKEND",
            "Develops server-side applications using Java and Spring Boot.",
            new String[] {
                "Java", "Spring Boot", "Spring Framework",
                "REST API", "MySQL", "Hibernate",
                "Spring Data JPA", "Git", "Maven"
            }
        );

        createRole(
            "Frontend Developer",
            "FRONTEND",
            "Develops user interfaces for web applications.",
            new String[] {
                "HTML5", "CSS3", "JavaScript",
                "React", "Bootstrap", "Responsive Design",
                "DOM Manipulation", "Git"
            }
        );

        createRole(
            "Full Stack Developer",
            "FULL_STACK",
            "Develops both frontend and backend web applications.",
            new String[] {
                "HTML5", "CSS3", "JavaScript",
                "React", "Java", "Spring Boot",
                "REST API", "MySQL", "Git"
            }
        );
        
        updateOptionalSkills();
    }

    private void createRole(
            String roleName,
            String roleCode,
            String description,
            String[] skills) {

        CareerRole role = careerRoleRepository
                .findAll()
                .stream()
                .filter(r -> r.getRoleCode().equals(roleCode))
                .findFirst()
                .orElseGet(CareerRole::new);

        role.setRoleName(roleName);
        role.setRoleCode(roleCode);
        role.setDescription(description);

        role = careerRoleRepository.save(role);

        for (String skill : skills) {

            boolean exists = roleSkillRepository
                    .findByCareerRoleId(role.getId())
                    .stream()
                    .anyMatch(s -> s.getSkillName().equalsIgnoreCase(skill));

            if (!exists) {
                RoleSkill roleSkill = new RoleSkill();

                roleSkill.setCareerRole(role);
                roleSkill.setSkillName(skill);
                roleSkill.setMandatory(
                	    !skill.equals("Maven") &&
                	    !skill.equals("Git")
                	);

                roleSkillRepository.save(roleSkill);
            }
        }
    }
    
    
    private void updateOptionalSkills() {

        for (RoleSkill skill : roleSkillRepository.findAll()) {

        	if ("Git".equalsIgnoreCase(skill.getSkillName()) ||
        		"Maven".equalsIgnoreCase(skill.getSkillName())) {

                skill.setMandatory(false);
                roleSkillRepository.save(skill);
            }
        }
    }
}
