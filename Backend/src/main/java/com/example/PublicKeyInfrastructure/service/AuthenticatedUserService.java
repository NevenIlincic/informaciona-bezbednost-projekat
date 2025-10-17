package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.dto.authenticatedUser.CreateAuthenticatedUserDTO;
import com.example.PublicKeyInfrastructure.model.AuthenticatedUser;
import com.example.PublicKeyInfrastructure.model.Organization;
import com.example.PublicKeyInfrastructure.model.Role;
import com.example.PublicKeyInfrastructure.repository.AuthenticatedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthenticatedUserService {
    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired EmailService emailService;

    public AuthenticatedUser createUser(CreateAuthenticatedUserDTO userToCreate) {
        Organization foundOrganization = organizationService.findOrganizationById(userToCreate.getOrganization().getId());
        AuthenticatedUser userToSave = new AuthenticatedUser();
        userToSave.setEmail(userToCreate.getEmail());
        userToSave.setFirstName(userToCreate.getFirstName());
        userToSave.setLastName(userToCreate.getLastName());
        userToSave.setRole(Role.REGULAR_USER);
        userToSave.setOrganization(foundOrganization);
        userToSave.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
        userToSave.setIsActive(false);

        String activationToken = UUID.randomUUID().toString();
        userToSave.setActivationToken(activationToken);
        userToSave.setTokenExpiry(LocalDateTime.now().plusHours(24));
        emailService.sendActivationEmail(userToCreate.getEmail(), activationToken);

        return authenticatedUserRepository.save(userToSave);
    }

    public AuthenticatedUser findUserByEmail(String email) {
        return authenticatedUserRepository.findByEmail(email).orElse(null);
    }

    public AuthenticatedUser activateAccount(String token) {
        AuthenticatedUser userToActivate = findUserByActivationToken(token);
        if (userToActivate.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return null;
        }
        if (userToActivate.getIsActive()) {
            return null;
        }
        userToActivate.setIsActive(true);
        authenticatedUserRepository.save(userToActivate);
        return userToActivate;
    }

    private AuthenticatedUser findUserByActivationToken(String token) {
        return authenticatedUserRepository.findByActivationToken(token).orElse(null);
    }
}