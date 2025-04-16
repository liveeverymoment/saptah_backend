package org.saptah.main.adminuser.repository;

import org.saptah.main.adminuser.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminUserJPARepository extends JpaRepository<AdminUser, Long> {
    boolean existsByEmail(String email);

    AdminUser findByEmailAndPassword(String email, String password);

    @Query("SELECT u FROM AdminUser u LEFT JOIN FETCH u.verificationTokens WHERE u.id = :id")
    Optional<AdminUser> findByIdWithTokens(@Param("id") Long id);
}
