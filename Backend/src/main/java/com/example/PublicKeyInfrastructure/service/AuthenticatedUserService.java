package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.repository.AuthenticatedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService {
    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;
}
