package com.example.PublicKeyInfrastructure.controller;

import com.example.PublicKeyInfrastructure.dto.authenticatedUser.CreateAuthenticatedUserDTO;
import com.example.PublicKeyInfrastructure.dto.organization.RegistrationOrganizationDTO;
import com.example.PublicKeyInfrastructure.service.AuthenticatedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class AuthenticatedUserController {

    @Autowired
    private AuthenticatedUserService authenticatedUserService;
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody CreateAuthenticatedUserDTO userToCreate) {
        try {
            authenticatedUserService.createUser(userToCreate);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
