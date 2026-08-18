package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.model.SmsVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsVerificationRepository extends JpaRepository<SmsVerification,Long> {
    SmsVerification findByPhone(Long phone);
}
