package com.example.PublicKeyInfrastructure.controller;

import com.example.PublicKeyInfrastructure.dto.certificate.CertificateDTO;
import com.example.PublicKeyInfrastructure.dto.certificate.EECertificateDTO;
import com.example.PublicKeyInfrastructure.dto.certificate.IntermediateCertificateDTO;
import com.example.PublicKeyInfrastructure.model.AdminMasterKey;
import com.example.PublicKeyInfrastructure.service.AdminMasterKeyService;
import com.example.PublicKeyInfrastructure.service.CertificateService;
import com.example.PublicKeyInfrastructure.utils.AESUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createRootCertificate(@RequestBody CertificateDTO certificateDTO) {
//        getMasterKey();
       // saveMasterKey();
        certificateService.createRootCertificate(certificateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/intermediate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createIntermediateCertificate(@RequestBody IntermediateCertificateDTO intermediateCertificateDTO){
        certificateService.createIntermediateCertificate(intermediateCertificateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/end-entity", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createEndEntityCertificate(@RequestBody EECertificateDTO eeCertificateDTO){
        certificateService.createEndEntityCertificate(eeCertificateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    private void saveMasterKey(){
        String masterKey = aesUtils.generateMasterKey();
        String encryptedMasterKey = aesUtils.encrypt(masterKey);
        System.out.println(encryptedMasterKey);
//        AdminMasterKey adminMasterKey = new AdminMasterKey(null, encryptedMasterKey);
//        adminMasterKeyService.saveMasterKey(adminMasterKey);
    }

    private void getMasterKey(){
        AdminMasterKey adminMasterKey = adminMasterKeyService.getMasterKey();
        System.out.println(adminMasterKey.getMasterKey());
    }
}
