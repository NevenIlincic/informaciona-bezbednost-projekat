package com.example.PublicKeyInfrastructure.dto.organization;

import com.example.PublicKeyInfrastructure.model.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrganizationDTO {
    private Integer id;
    private String name;
    private GetOrganizationDTO parentOrganization;
    private String masterKeyEncrypted;

    public GetOrganizationDTO(Organization organization) {
        id = organization.getId();
        name = organization.getName();
        if (organization.getParentOrganization() != null) {
            parentOrganization = new GetOrganizationDTO(organization.getParentOrganization());
        } else {
            parentOrganization = null;
        }
        masterKeyEncrypted = organization.getMasterKeyEncrypted();
    }
}
