package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.model.Organization;
import com.example.PublicKeyInfrastructure.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public Collection<Organization> getAllOrganizations() { return organizationRepository.findAll(); }

    public Organization findOrganizationById(int id){
        return organizationRepository.findById(id).get();
    }
}
