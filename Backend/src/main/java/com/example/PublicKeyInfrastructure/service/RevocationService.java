package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.repository.RevocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RevocationService {
    @Autowired
    private RevocationRepository revocationRepository;
}
