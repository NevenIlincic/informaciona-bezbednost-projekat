package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.dto.certificate.CertificateDTO;
import com.example.PublicKeyInfrastructure.model.AuthenticatedUser;
import com.example.PublicKeyInfrastructure.model.Certificate;
import com.example.PublicKeyInfrastructure.model.CertificateType;
import com.example.PublicKeyInfrastructure.model.Organization;
import com.example.PublicKeyInfrastructure.repository.CertificateRepository;
import com.example.PublicKeyInfrastructure.utils.AESUtils;
import com.example.PublicKeyInfrastructure.utils.CertificateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private CertificateUtils certificateUtils;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private AuthenticatedUserService authenticatedUserService;
    @Autowired
    private AdminMasterKeyService adminMasterKeyService;
    @Autowired
    private AESUtils aesUtils;

    public Certificate createRootCertificate(){
        LocalDateTime validFrom = LocalDateTime.of(2025, 8, 12, 0, 0);
        LocalDateTime validTo = LocalDateTime.of(2025, 12, 12, 0, 0);
        CertificateDTO certificateDTO = new CertificateDTO();
        certificateDTO.setSerialNumber("30006");
        certificateDTO.setSubjectCommonName("Pera");
        certificateDTO.setSubjectOrganizationName("Root Org");
        certificateDTO.setSubjectOrganizationalUnit("Organizational Unit");
        certificateDTO.setSubjectEmail("pera@gmail.com");
        certificateDTO.setSubjectCountry("RS");

        Certificate certificate = new Certificate();
        certificate.setType(CertificateType.ROOT);
        certificate.setSerialNumber(certificateDTO.getSerialNumber());
        certificate.setSubjectCommonName(certificateDTO.getSubjectCommonName());
        Organization organization = organizationService.findOrganizationByName(certificateDTO.getSubjectOrganizationName());
        certificate.setOrganization(organization);
        AuthenticatedUser user = authenticatedUserService.findUserByEmail(certificateDTO.getSubjectEmail());
        certificate.setCAuser(user);
        certificate.setIssuerCertificate(null);
        certificate.setValidFrom(validFrom);
        certificate.setValidTo(validTo);
        certificate.setSubjectCommonName(certificateDTO.getSubjectCommonName());
        certificate.setSubjectOrganization(certificateDTO.getSubjectOrganizationName());
        certificate.setSubjectOrganizationalUnit(certificateDTO.getSubjectOrganizationalUnit());
        certificate.setSubjectEmail(certificateDTO.getSubjectEmail());
        certificate.setSubjectCountry(certificateDTO.getSubjectCountry());
        Map<String, Object> issuerData = new HashMap<>();
        issuerData.put("nesto", 2);
        certificate.setIssuerData(issuerData);
        certificate.setCsrPem(null);
        certificate.setIsRevoked(false);
        certificate.setRevocationDate(null);
        certificate.setRevocationReason(null);

        Map<String, String> issuerCertificateData = new HashMap<>();

        try{
            KeyPair keyPair = certificateUtils.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                    Base64.getEncoder().encodeToString(publicKey.getEncoded()) +
                    "\n-----END PUBLIC KEY-----";
            PrivateKey privateKey = keyPair.getPrivate();
            String adminMasterKeyDecrypted = aesUtils.decrypt(adminMasterKeyService.getMasterKey().getMasterKey());
            String privateKeyEncrypted = aesUtils.encryptPrivateKey(privateKey, adminMasterKeyDecrypted);
            certificate.setPrivateKey(privateKeyEncrypted);
            X509Certificate certificateX509 = certificateUtils.generateCertificate(certificateDTO, publicKey, privateKey, null, validFrom, validTo);
            String certificatePEM = certificateUtils.convertToPem(certificateX509);
            System.out.println(certificatePEM);
            certificate.setPublicKeyPem(publicKeyPem);
            certificate.setCertificatePem(certificatePEM);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return certificateRepository.save(certificate);
    }

}
