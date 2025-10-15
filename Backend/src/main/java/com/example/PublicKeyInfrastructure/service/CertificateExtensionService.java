package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.repository.CertificateExtensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateExtensionService {
    @Autowired
    private CertificateExtensionRepository certificateExtensionRepository;
}
