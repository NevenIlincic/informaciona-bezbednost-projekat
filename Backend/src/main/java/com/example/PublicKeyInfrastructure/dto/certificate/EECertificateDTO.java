package com.example.PublicKeyInfrastructure.dto.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EECertificateDTO {

    private int issuerCertificateId;
    private String passwordForCertificate;
    private String subjectCommonName;
    private String subjectOrganizationName;
    private String subjectOrganizationalUnit;
    private String subjectCountry;
    private String subjectEmail;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private boolean isDigitalSignature;
    private boolean isKeyEncipherment;
    private boolean isServerAuth;
    private boolean isClientAuth;
}
