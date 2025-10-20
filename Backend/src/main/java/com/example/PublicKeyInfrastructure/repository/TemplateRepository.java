package com.example.PublicKeyInfrastructure.repository;

import com.example.PublicKeyInfrastructure.model.AuthenticatedUser;
import com.example.PublicKeyInfrastructure.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Integer> {
    Optional<Template> findById(int id);

    Collection<Template> findByCaIssuer(AuthenticatedUser caIssuer);
}
