package com.example.PublicKeyInfrastructure.dto.authenticatedUser;
import com.example.PublicKeyInfrastructure.dto.organization.RegistrationOrganizationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuthenticatedUserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RegistrationOrganizationDTO organization;
}
