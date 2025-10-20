package com.example.PublicKeyInfrastructure.controller;

import com.example.PublicKeyInfrastructure.dto.authenticatedUser.CreateAuthenticatedUserDTO;
import com.example.PublicKeyInfrastructure.dto.template.CreateTemplateDTO;
import com.example.PublicKeyInfrastructure.dto.template.CreatedTemplateDTO;
import com.example.PublicKeyInfrastructure.dto.template.GetTemplateDTO;
import com.example.PublicKeyInfrastructure.model.AuthenticatedUser;
import com.example.PublicKeyInfrastructure.model.Template;
import com.example.PublicKeyInfrastructure.service.AuthenticatedUserService;
import com.example.PublicKeyInfrastructure.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api/templates")
@CrossOrigin
public class TemplateController {
    
    @Autowired
    private TemplateService templateService;
    @Autowired
    private AuthenticatedUserService authenticatedUserService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GetTemplateDTO>> getAllTemplates() {
        Collection<Template> templates = templateService.findAll();
        Collection<GetTemplateDTO> templateDTOs = new ArrayList<>();

        for (Template template : templates) {
            templateDTOs.add(new GetTemplateDTO(template));
        }

        return new ResponseEntity<Collection<GetTemplateDTO>>(templateDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetTemplateDTO> getTemplate(@PathVariable("id") Integer id) {
        Template template = templateService.findById(id);

        if (template == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new GetTemplateDTO(template), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GetTemplateDTO>> getTemplatesByUser(@PathVariable("id") Integer id) {
        AuthenticatedUser user = authenticatedUserService.findUserById(id);
        Collection<Template> templates = templateService.findTemplatesByUser(user);

        Collection<GetTemplateDTO> templateDTOs = new ArrayList<>();

        for (Template template : templates) {
            templateDTOs.add(new GetTemplateDTO(template));
        }

        return new ResponseEntity<Collection<GetTemplateDTO>>(templateDTOs, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedTemplateDTO> createTemplate(@RequestBody CreateTemplateDTO userToCreate) {
        Template template = templateService.createTemplate(userToCreate);
        return new ResponseEntity<>(new CreatedTemplateDTO(template), HttpStatus.CREATED);
    }
}
