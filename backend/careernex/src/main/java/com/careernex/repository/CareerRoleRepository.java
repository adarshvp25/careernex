
package com.careernex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.careernex.entity.CareerRole;

public interface CareerRoleRepository
        extends JpaRepository<CareerRole, Long> {

}
