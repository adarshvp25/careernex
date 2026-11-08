
package com.careernex.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "role_skills",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"career_role_id", "skill_name"}
        )
    }
)
public class RoleSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_role_id", nullable = false)
    private CareerRole careerRole;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(nullable = false)
    private Boolean mandatory = true;

    public RoleSkill() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CareerRole getCareerRole() {
        return careerRole;
    }

    public void setCareerRole(CareerRole careerRole) {
        this.careerRole = careerRole;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }
}
