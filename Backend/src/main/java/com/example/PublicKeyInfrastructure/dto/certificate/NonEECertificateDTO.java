package com.example.PublicKeyInfrastructure.dto.certificate;

import com.example.PublicKeyInfrastructure.model.Certificate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NonEECertificateDTO {
    private int id;
    private String serialNumber;
    private String subjectCommonName;

    public NonEECertificateDTO(Certificate certificate){
        this.id = certificate.getId();
        this.serialNumber = certificate.getSerialNumber();
        this.subjectCommonName = certificate.getSubjectCommonName();
    }
}
