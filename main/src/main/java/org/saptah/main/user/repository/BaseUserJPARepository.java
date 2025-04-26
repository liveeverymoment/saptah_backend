package org.saptah.main.user.repository;

import org.saptah.main.user.entity.AdminUser;
import org.saptah.main.user.entity.BaseUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseUserJPARepository extends JpaRepository<AdminUser, Long> {
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "authorities") // loads roles in same query
    Optional<BaseUser> findByEmail(String email);

    @Query("SELECT u FROM BaseUser u LEFT JOIN FETCH u.verificationTokens WHERE u.id = :id")
    Optional<BaseUser> findByIdWithTokens(@Param("id") Long id);
}
