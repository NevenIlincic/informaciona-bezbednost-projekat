package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.dto.authenticatedUser.CreateAuthenticatedUserDTO;
import com.example.PublicKeyInfrastructure.model.AuthenticatedUser;
import com.example.PublicKeyInfrastructure.model.Organization;
import com.example.PublicKeyInfrastructure.model.Role;
import com.example.PublicKeyInfrastructure.repository.AuthenticatedUserRepository;
import com.example.PublicKeyInfrastructure.utils.AESUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticatedUserService {
    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired EmailService emailService;
    @Autowired
    private AESUtils aesUtils;
    @Autowired
    private ActivationTokenService activationTokenService;

    public AuthenticatedUser createUser(CreateAuthenticatedUserDTO userToCreate) throws IllegalArgumentException {
        validatePassword(userToCreate);
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

    public AuthenticatedUser findUserById(int id) {
        return authenticatedUserRepository.findById(id).orElse(null);
    }

    public AuthenticatedUser activateAccount(String tokenEncrypted) {
        String token = aesUtils.decrypt(tokenEncrypted);

        if (activationTokenService.getByActivationToken(token) != null) {
            return null;
        }
        AuthenticatedUser userToActivate = findUserByActivationToken(token);
        if (userToActivate.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return null;
        }
        if (userToActivate.getIsActive()) {
            return null;
        }
        userToActivate.setIsActive(true);
        authenticatedUserRepository.save(userToActivate);
        this.activationTokenService.saveToken(token);
        return userToActivate;
    }

    private AuthenticatedUser findUserByActivationToken(String token) {
        return authenticatedUserRepository.findByActivationToken(token).orElse(null);
    }

    private void validatePassword(CreateAuthenticatedUserDTO userToCreate) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@$!%*?&]).{8,}$");
        Matcher matcher = pattern.matcher(userToCreate.getPassword());
        if (!matcher.find()) {
            throw new IllegalArgumentException("Password doesn't meet the requirements!");
        }
        if (!userToCreate.getPassword().equals(userToCreate.getRepeatedPassword())) {
            throw new IllegalArgumentException("Passwords do not match!");
        }
    }
}