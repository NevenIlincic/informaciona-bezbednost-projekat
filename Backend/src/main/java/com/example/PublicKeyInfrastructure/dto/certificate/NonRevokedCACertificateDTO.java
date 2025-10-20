package com.example.PublicKeyInfrastructure.dto.certificate;

import com.example.PublicKeyInfrastructure.model.Certificate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NonRevokedCACertificateDTO {

    private int id;
    private String serialNumber;
    private String subjectCommonName;

    public NonRevokedCACertificateDTO(Certificate certificate){
        this.id = certificate.getId();
        this.serialNumber = certificate.getSerialNumber();
        this.subjectCommonName = certificate.getSubjectCommonName();
    }
}
