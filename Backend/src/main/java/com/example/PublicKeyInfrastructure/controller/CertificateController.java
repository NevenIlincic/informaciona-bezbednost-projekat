package com.example.PublicKeyInfrastructure.controller;

import com.example.PublicKeyInfrastructure.dto.certificate.*;
import com.example.PublicKeyInfrastructure.model.AdminMasterKey;
import com.example.PublicKeyInfrastructure.model.Certificate;
import com.example.PublicKeyInfrastructure.service.AdminMasterKeyService;
import com.example.PublicKeyInfrastructure.service.CertificateService;
import com.example.PublicKeyInfrastructure.utils.AESUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

    @GetMapping(value = "/admin")
    public ResponseEntity<List<CertificateTabDTO>> getAllCertificates(){
        try {
            List<CertificateTabDTO> certificateTabDTOS = this.certificateService.getAllCertificates();
            return new ResponseEntity<>(certificateTabDTOS, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

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

    @PostMapping(value = "/end-entity", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createEndEntityCertificate(@RequestBody EECertificateDTO eeCertificateDTO, @RequestParam(value = "isAdminCreating") boolean isAdminCreating){
        try{
            byte[] pcks12bytes = certificateService.createEndEntityCertificate(eeCertificateDTO, isAdminCreating );
            String encoded =  Base64.getEncoder().encodeToString(pcks12bytes);
            Pcks12DTO pcks12DTO = new Pcks12DTO(encoded, "End_Entity_Certificate");
            return new ResponseEntity<>(pcks12DTO,HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
//            return new ResponseEntity<>(new Pcks12DTO("Invalid", "Invalid"),HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new Pcks12DTO(e.getMessage(), "Invalid"),HttpStatus.BAD_REQUEST);


        }
    }

    @GetMapping(value = "/intermediate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NonRevokedCACertificateDTO>> getNonRevokedCACertificates(){
        List<Certificate> foundCertificates = certificateService.findNonRevokedCACertificates();
        List<NonRevokedCACertificateDTO> nonRevokedCACertificateDTOList = new ArrayList<>();
        for (Certificate certificate : foundCertificates) {
            nonRevokedCACertificateDTOList.add(new NonRevokedCACertificateDTO(certificate));
        }

        return new ResponseEntity<>(nonRevokedCACertificateDTOList, HttpStatus.OK);
    }

    @GetMapping(value = "/end-entity/user/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RegularUserCertificateDTO>> getRegularUserCertificate(@PathVariable String email){
        List<RegularUserCertificateDTO> foundCertificatesDTO = this.certificateService.getRegularUserCertificateDTO(email);
        return new ResponseEntity<>(foundCertificatesDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/download")
    public ResponseEntity<Pcks12DTO> downloadCertificate(@RequestBody DownloadCertificateDTO downloadCertificateDTO){
        try{
            byte[] pcks12bytes = certificateService.downloadCertificate(downloadCertificateDTO);
            String encoded =  Base64.getEncoder().encodeToString(pcks12bytes);
            Pcks12DTO pcks12DTO = new Pcks12DTO(encoded, "Downloaded Certificate");
            return new ResponseEntity<>(pcks12DTO,HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(new Pcks12DTO("Invalid", "Invalid"),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/revoke")
    public ResponseEntity<?> revokeCertificate(@RequestBody RevocationDTO revocationDTO){
        this.certificateService.revokeCertificate(revocationDTO.getId(), revocationDTO.getRevocationReason());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/non-end-entity")
    public ResponseEntity<List<NonEECertificateDTO>> getAllNonEECertificates(){
        List<Certificate> foundCertificates = certificateService.findNonEECertificates();
        System.out.println(foundCertificates.size());
        List<NonEECertificateDTO> nonEECertificatesDTO = new ArrayList<>();
        for (Certificate certificate : foundCertificates) {
            nonEECertificatesDTO.add(new NonEECertificateDTO(certificate));
        }
        return new ResponseEntity<>(nonEECertificatesDTO, HttpStatus.OK);
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
