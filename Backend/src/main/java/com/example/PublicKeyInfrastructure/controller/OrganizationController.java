package com.example.PublicKeyInfrastructure.controller;

import com.example.PublicKeyInfrastructure.model.Organization;
import com.example.PublicKeyInfrastructure.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.PublicKeyInfrastructure.dto.organization.GetOrganizationDTO;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api/organizations")
@CrossOrigin
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GetOrganizationDTO>> getAllOrganizations() {
        Collection<Organization> organizations = organizationService.getAllOrganizations();
        Collection<GetOrganizationDTO> organizationDTOs = new ArrayList<>();

        for (Organization organization : organizations) {
            organizationDTOs.add(new GetOrganizationDTO(organization));
        }

        return new ResponseEntity<Collection<GetOrganizationDTO>>(organizationDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetOrganizationDTO> getOrganization(@PathVariable("id") Integer id) {
        Organization organization = organizationService.findOrganizationById(id);

        if (organization == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new GetOrganizationDTO(organization), HttpStatus.OK);
    }
}
