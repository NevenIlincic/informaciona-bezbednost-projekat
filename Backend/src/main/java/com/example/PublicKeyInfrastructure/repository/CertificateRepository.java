package com.example.PublicKeyInfrastructure.repository;

import com.example.PublicKeyInfrastructure.model.Certificate;
import com.example.PublicKeyInfrastructure.model.CertificateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

    @Query("SELECT e FROM Certificate e " +
            "WHERE e.isRevoked = false AND e.type = :certificateType ")
    List<Certificate> returnNonRevokedCertificates(CertificateType certificateType);

    List<Certificate> findCertificatesBySubjectEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Certificate c " +
            "SET c.isRevoked = true, " +
            "c.revocationReason = :reason, " +
            "c.revocationDate = :date " +
            "WHERE c.id = :id")
    void revokeCertificate(@Param("id") int id, @Param("reason") String revocationReason, @Param("date")LocalDateTime revocationDate);
}
