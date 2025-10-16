package com.example.PublicKeyInfrastructure.dto.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDTO {

    private String subjectCommonName;
    private String subjectOrganizationName;
    private String subjectOrganizationalUnit;
    private String subjectCountry;
    private String subjectEmail;
    private String serialNumber;
}
