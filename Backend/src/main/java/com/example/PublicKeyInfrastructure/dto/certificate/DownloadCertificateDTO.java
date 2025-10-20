package com.example.PublicKeyInfrastructure.dto.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadCertificateDTO {

    private int id;
    private String pkcs12password;
}
