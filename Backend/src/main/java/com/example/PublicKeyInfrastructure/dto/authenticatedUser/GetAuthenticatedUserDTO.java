package com.example.PublicKeyInfrastructure.dto.authenticatedUser;

import com.example.PublicKeyInfrastructure.model.AuthenticatedUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAuthenticatedUserDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private int organizationId;

    public GetAuthenticatedUserDTO(AuthenticatedUser user){
        this.id =  user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.organizationId = user.getOrganization().getId();
    }
}
