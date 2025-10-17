package com.example.PublicKeyInfrastructure.repository;

import com.example.PublicKeyInfrastructure.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    Optional<Organization> findOrganizationByName(String name);
}
