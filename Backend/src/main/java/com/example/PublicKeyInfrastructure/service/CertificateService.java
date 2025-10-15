package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;
}
