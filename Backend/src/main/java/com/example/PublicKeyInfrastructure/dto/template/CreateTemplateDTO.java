package com.example.PublicKeyInfrastructure.dto.template;

import com.example.PublicKeyInfrastructure.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.PublicKeyInfrastructure.dto.certificate.CertificateDTO;
import com.example.PublicKeyInfrastructure.dto.certificate.GetCertificateDTO;
import com.example.PublicKeyInfrastructure.model.Certificate;
import com.example.PublicKeyInfrastructure.model.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTemplateDTO {
    private String name;
    private int organizationId;
    private GetAuthenticatedUserDTO caIssuer;
    private String cnRegex;
    private String sanRegex;
    private Integer maxTtlDays;
    private String defaultKeyUsage;
    private String defaultExtendedKeyUsage;
}
