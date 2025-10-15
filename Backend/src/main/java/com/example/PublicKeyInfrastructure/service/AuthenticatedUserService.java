package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.dto.authenticatedUser.CreateAuthenticatedUserDTO;
import com.example.PublicKeyInfrastructure.model.AuthenticatedUser;
import com.example.PublicKeyInfrastructure.model.Organization;
import com.example.PublicKeyInfrastructure.model.Role;
import com.example.PublicKeyInfrastructure.repository.AuthenticatedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService {
    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;
    @Autowired
    private OrganizationService organizationService;

    public AuthenticatedUser createUser(CreateAuthenticatedUserDTO userToCreate) {
        Organization foundOrganization = organizationService.findOrganizationById(userToCreate.getOrganization().getId());
        AuthenticatedUser userToSave = new AuthenticatedUser();
        userToSave.setEmail(userToCreate.getEmail());
        userToSave.setFirstName(userToCreate.getFirstName());
        userToSave.setLastName(userToCreate.getLastName());
        userToSave.setRole(Role.REGULAR_USER);
        userToSave.setOrganization(foundOrganization);
        userToSave.setPassword(userToCreate.getPassword());
        userToSave.setIsActive(true);

        return authenticatedUserRepository.save(userToSave);

    }
}