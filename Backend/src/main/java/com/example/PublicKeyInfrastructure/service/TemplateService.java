package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {
    @Autowired
    private TemplateRepository templateRepository;
}
