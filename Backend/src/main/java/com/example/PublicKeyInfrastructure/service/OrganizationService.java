package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.model.Organization;
import com.example.PublicKeyInfrastructure.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public Organization findOrganizationById(int id){
        return organizationRepository.findById(id).get();
    }

    public Organization findOrganizationByName(String name){
        return organizationRepository.findOrganizationByName(name).orElse(null);
    }
}
