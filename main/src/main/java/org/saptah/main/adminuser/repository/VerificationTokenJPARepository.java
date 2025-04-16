package org.saptah.main.adminuser.repository;

import org.saptah.main.adminuser.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenJPARepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
