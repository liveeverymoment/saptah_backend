package org.saptah.main.user.repository;

import org.saptah.main.user.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenJPARepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
