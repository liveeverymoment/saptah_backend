package org.saptah.main.adminuser.repository;

import org.saptah.main.adminuser.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserJPARepository extends JpaRepository<AdminUser, Long> {
    boolean existsByEmail(String email);
}
