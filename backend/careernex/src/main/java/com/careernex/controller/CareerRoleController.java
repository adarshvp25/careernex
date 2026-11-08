
package com.careernex.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.careernex.entity.CareerRole;
import com.careernex.repository.CareerRoleRepository;
import com.careernex.repository.RoleSkillRepository;

@RestController
@RequestMapping("/api/roles")
public class CareerRoleController {

    private final CareerRoleRepository careerRoleRepository;
    private final RoleSkillRepository roleSkillRepository;

    public CareerRoleController(
            CareerRoleRepository careerRoleRepository,
            RoleSkillRepository roleSkillRepository) {

        this.careerRoleRepository = careerRoleRepository;
        this.roleSkillRepository = roleSkillRepository;
    }

    @GetMapping
    public List<Map<String, Object>> getAllRoles() {

        return careerRoleRepository.findAll()
                .stream()
                .map(role -> Map.<String, Object>of(
                        "id", role.getId(),
                        "roleName", role.getRoleName(),
                        "roleCode", role.getRoleCode(),
                        "description",
                        role.getDescription() == null
                                ? ""
                                : role.getDescription()
                ))
                .toList();
    }
    
    
    @GetMapping("/{roleId}/skills")
    public List<Map<String, Object>> getRoleSkills(
            @PathVariable Long roleId) {

        return roleSkillRepository.findByCareerRoleId(roleId)
                .stream()
                .map(skill -> Map.<String, Object>of(
                        "id", skill.getId(),
                        "skillName", skill.getSkillName(),
                        "mandatory", skill.getMandatory()
                ))
                .toList();
    }
}
