package com.project.mt.repository;

import com.project.mt.domain.EmailCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailCertificateRepository extends JpaRepository<EmailCertificate, Long> {
    void deleteByEmail(String email);
}
