package com.team.veco.repository;

import com.team.veco.domain.EmailCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailCertificateRepository extends JpaRepository<EmailCertificate, Long> {
    void deleteByEmail(String email);
}
