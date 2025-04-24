package org.saptah.main.adminuser.repository;

import org.saptah.main.adminuser.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUserJPARepository extends JpaRepository<AdminUser, Long> {
    boolean existsByEmail(String email);

    AdminUser findByEmail(String email);

    @Query("SELECT u FROM AdminUser u LEFT JOIN FETCH u.verificationTokens WHERE u.id = :id")
    Optional<AdminUser> findByIdWithTokens(@Param("id") Long id);
}
