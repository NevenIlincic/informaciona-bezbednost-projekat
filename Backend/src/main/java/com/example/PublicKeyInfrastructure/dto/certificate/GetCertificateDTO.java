package com.example.PublicKeyInfrastructure.dto.certificate;

import com.example.PublicKeyInfrastructure.model.AuthenticatedUser;
import com.example.PublicKeyInfrastructure.model.Certificate;
import com.example.PublicKeyInfrastructure.model.CertificateType;
import com.example.PublicKeyInfrastructure.model.Organization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCertificateDTO {
    private Integer id;
    private CertificateType type;
    private String certificatePem;
    private String privateKey;
    private Organization organization;
    private AuthenticatedUser CAuser;
    private Certificate issuerCertificate;
    private String serialNumber;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private String subjectCommonName;
    private String subjectOrganization;
    private String subjectOrganizationalUnit;
    private String subjectCountry;
    private String subjectEmail;
    private Map<String, String> issuerData;
    private String publicKeyPem;
    private String csrPem;
    private Boolean isRevoked;
    private LocalDateTime revocationDate;
    private String revocationReason;

    public GetCertificateDTO(Certificate certificate) {
        id = certificate.getId();
        type = certificate.getType();
        certificatePem = certificate.getCertificatePem();
        privateKey = certificate.getPrivateKey();
        organization = certificate.getOrganization();
        CAuser = certificate.getCAuser();
        issuerCertificate = certificate.getIssuerCertificate();
        serialNumber = certificate.getSerialNumber();
        validFrom = certificate.getValidFrom();
        validTo = certificate.getValidTo();
        subjectCommonName = certificate.getSubjectCommonName();
        subjectOrganization = certificate.getSubjectOrganization();
        subjectOrganizationalUnit = certificate.getSubjectOrganizationalUnit();
        subjectCountry = certificate.getSubjectCountry();
        subjectEmail = certificate.getSubjectEmail();
        issuerData = certificate.getIssuerData();
        publicKeyPem = certificate.getPublicKeyPem();
        csrPem = certificate.getCsrPem();
        isRevoked = certificate.getIsRevoked();
        revocationDate = certificate.getRevocationDate();
        revocationReason = certificate.getRevocationReason();
    }
}
