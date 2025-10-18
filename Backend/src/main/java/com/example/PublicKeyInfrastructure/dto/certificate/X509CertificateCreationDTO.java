package com.example.PublicKeyInfrastructure.dto.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.security.SecureRandom;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class X509CertificateCreationDTO {
    private String subjectCommonName;
    private String subjectOrganizationName;
    private String subjectOrganizationalUnit;
    private String subjectCountry;
    private String subjectEmail;
    private BigInteger serialNumber;

    public X509CertificateCreationDTO(CertificateDTO certificateDTO) {
        this.subjectCommonName = certificateDTO.getSubjectCommonName();
        this.subjectOrganizationName = certificateDTO.getSubjectOrganizationName();
        this.subjectOrganizationalUnit = certificateDTO.getSubjectOrganizationalUnit();
        this.subjectCountry = certificateDTO.getSubjectCountry();
        this.subjectEmail = certificateDTO.getSubjectEmail();
        this.serialNumber = new BigInteger(certificateDTO.getSerialNumber());
    }

    public X509CertificateCreationDTO(IntermediateCertificateDTO certificateDTO) {
        this.subjectCommonName = certificateDTO.getSubjectCommonName();
        this.subjectOrganizationName = certificateDTO.getSubjectOrganizationName();
        this.subjectOrganizationalUnit = certificateDTO.getSubjectOrganizationalUnit();
        this.subjectCountry = certificateDTO.getSubjectCountry();
        this.subjectEmail = certificateDTO.getSubjectEmail();
        this.serialNumber = new BigInteger(certificateDTO.getSerialNumber());
    }
    public X509CertificateCreationDTO(EECertificateDTO eeCertificateDTO) {
        this.subjectCommonName = eeCertificateDTO.getSubjectCommonName();
        this.subjectOrganizationName = eeCertificateDTO.getSubjectOrganizationName();
        this.subjectOrganizationalUnit = eeCertificateDTO.getSubjectOrganizationalUnit();
        this.subjectCountry = eeCertificateDTO.getSubjectCountry();
        this.subjectEmail = eeCertificateDTO.getSubjectEmail();
        SecureRandom random = new SecureRandom();
        // 64-bit random broj, dovoljno za jedinstvenost
        this.serialNumber = new BigInteger(64, random);
    }

}
