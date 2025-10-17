package com.example.PublicKeyInfrastructure.controller;

import com.example.PublicKeyInfrastructure.model.AdminMasterKey;
import com.example.PublicKeyInfrastructure.service.AdminMasterKeyService;
import com.example.PublicKeyInfrastructure.service.CertificateService;
import com.example.PublicKeyInfrastructure.utils.AESUtils;
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
    @Autowired
    private AdminMasterKeyService adminMasterKeyService;
    @Autowired
    private AESUtils aesUtils;

    @PostMapping(value = "/root")
    public ResponseEntity<?> createRootCertificate(){
//        getMasterKey();
//        saveMasterKey();
        certificateService.createRootCertificate();
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
    private void saveMasterKey(){
        String masterKey = aesUtils.generateMasterKey();
        String encryptedMasterKey = aesUtils.encrypt(masterKey);
        AdminMasterKey adminMasterKey = new AdminMasterKey(null, encryptedMasterKey);
        adminMasterKeyService.saveMasterKey(adminMasterKey);
    }

    private void getMasterKey(){
        AdminMasterKey adminMasterKey = adminMasterKeyService.getMasterKey();
        System.out.println(adminMasterKey.getMasterKey());
    }
}
