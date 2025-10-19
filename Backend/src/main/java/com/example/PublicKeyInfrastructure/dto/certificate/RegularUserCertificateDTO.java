package com.example.PublicKeyInfrastructure.dto.certificate;

import com.example.PublicKeyInfrastructure.model.Certificate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegularUserCertificateDTO {

    private int id;
    private String subjectCommonName;
    private String subjectOrganizationName;
    private String subjectOrganizationalUnit;
    private String subjectCountry;
    private String subjectEmail;
    private String serialNumber;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private String digitalSignature;
    private String keyEncipherment;
    private boolean isRevoked;
    private LocalDateTime revocationDate;
    private String revocationReason;

    public RegularUserCertificateDTO(Certificate certificate){
        this.id = certificate.getId();
        this.subjectCommonName = certificate.getSubjectCommonName();
        this.subjectOrganizationName = certificate.getSubjectOrganization();
        this.subjectOrganizationalUnit = certificate.getSubjectOrganizationalUnit();
        this.subjectCountry = certificate.getSubjectCountry();
        this.subjectEmail = certificate.getSubjectEmail();
        this.serialNumber = certificate.getSerialNumber();
        this.validFrom = certificate.getValidFrom();
        this.validTo = certificate.getValidTo();
        this.digitalSignature = "";
        this.keyEncipherment = "";
        this.isRevoked = certificate.getIsRevoked();
        this.revocationDate = certificate.getRevocationDate();
        this.revocationReason = certificate.getRevocationReason();


    }
}
