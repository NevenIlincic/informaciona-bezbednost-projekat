package com.example.PublicKeyInfrastructure.dto.template;

import com.example.PublicKeyInfrastructure.dto.certificate.CertificateDTO;
import com.example.PublicKeyInfrastructure.dto.certificate.GetCertificateDTO;
import com.example.PublicKeyInfrastructure.model.Certificate;
import com.example.PublicKeyInfrastructure.model.Organization;
import com.example.PublicKeyInfrastructure.model.Template;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTemplateDTO {
    private Integer id;
    private String name;
    private Organization organization;
    private GetCertificateDTO caIssuer;
    private String cnRegex;
    private String sanRegex;
    private Integer maxTtlDays;
    private String defaultKeyUsage;
    private String defaultExtendedKeyUsage;

    public GetTemplateDTO(Template template) {
        id = template.getId();
        name = template.getName();
        organization = template.getOrganization();
        caIssuer = new GetCertificateDTO(template.getCaIssuer());
        cnRegex = template.getCnRegex();
        sanRegex = template.getSanRegex();
        maxTtlDays = template.getMaxTtlDays();
        defaultKeyUsage = template.getDefaultKeyUsage();
        defaultExtendedKeyUsage = template.getDefaultExtendedKeyUsage();
    }
}
