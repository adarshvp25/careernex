
package com.careernex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.careernex.entity.RoleSkill;

public interface RoleSkillRepository
        extends JpaRepository<RoleSkill, Long> {

    List<RoleSkill> findByCareerRoleId(Long careerRoleId);
}
