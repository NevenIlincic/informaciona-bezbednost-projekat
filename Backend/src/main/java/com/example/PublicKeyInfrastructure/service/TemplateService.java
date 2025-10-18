package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.dto.template.CreateTemplateDTO;
import com.example.PublicKeyInfrastructure.model.Certificate;
import com.example.PublicKeyInfrastructure.model.Organization;
import com.example.PublicKeyInfrastructure.model.Template;
import com.example.PublicKeyInfrastructure.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CertificateService certificateService;

    public Collection<Template> findAll() { return templateRepository.findAll(); }

    public Template findById(int id) { return templateRepository.findById(id).orElse(null); }

    public Template createTemplate(CreateTemplateDTO templateToCreate) {
        Organization foundOrganization = organizationService.findOrganizationById(templateToCreate.getOrganization().getId());
        Certificate foundCertificate = certificateService.findCertificateById(templateToCreate.getCaIssuer().getId());
        Template template = new Template();
        template.setName(templateToCreate.getName());
        template.setCaIssuer(foundCertificate);
        template.setOrganization(foundOrganization);
        template.setCnRegex(templateToCreate.getCnRegex());
        template.setSanRegex(templateToCreate.getSanRegex());
        template.setMaxTtlDays(templateToCreate.getMaxTtlDays());
        template.setDefaultKeyUsage(templateToCreate.getDefaultKeyUsage());
        template.setDefaultExtendedKeyUsage(templateToCreate.getDefaultExtendedKeyUsage());
        return templateRepository.save(template);
    }
}
