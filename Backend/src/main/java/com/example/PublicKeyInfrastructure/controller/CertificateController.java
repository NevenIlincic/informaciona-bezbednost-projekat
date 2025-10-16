package com.example.PublicKeyInfrastructure.controller;

import com.example.PublicKeyInfrastructure.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/certificates")
@CrossOrigin
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping(value = "/root")
    public ResponseEntity<?> createRootCertificate(){
        certificateService.createRootCertificate();
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
