package org.saptah.main.user.repository;

import org.saptah.main.user.entity.Role;
import org.saptah.main.user.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleJPARepository extends JpaRepository<Role,Long> {
    List<Role> findAllByTypeIn(List<RoleType> role_types);
}
