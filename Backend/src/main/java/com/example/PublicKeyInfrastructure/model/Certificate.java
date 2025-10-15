package com.example.PublicKeyInfrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type")
    private CertificateType type;
    @Column(name = "certificate_pem")
    private String certificatePem;
    @Column(name = "private_key_encrypted")
    private String privateKey;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AuthenticatedUser CAuser;
    @ManyToOne
    @JoinColumn(name = "issuer_cert_id")
    private Certificate issuerCertificate;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;
    @Column(name = "valid_to")
    private LocalDateTime validTo;
    @Column(name = "subject_common_name")
    private String subjectCommonName;
    @Column(name = "subject_organization")
    private String subjectOrganization;
    @Column(name = "subject_organization_unit")
    private String subjectOrganizationUnit;
    @Column(name = "subject_country")
    private String subjectCountry;
    @Column(name = "subject_email")
    private String subjectEmail;
    @Column(name = "issuer_data")
    private String issuerData;
    @Column(name = "public_key_pem")
    private String publicKeyPem;
    @Column(name = "csr_pem")
    private String csrPem;
    @Column(name = "is_revoked")
    private Boolean isRevoked;
    @Column(name = "revocation_date")
    private LocalDateTime revocationDate;
    @Column(name = "revocation_reason")
    private String revocationReason;

}
