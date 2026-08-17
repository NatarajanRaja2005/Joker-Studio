package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification,Long> {
    EmailVerification findByEmail(String email);
}
