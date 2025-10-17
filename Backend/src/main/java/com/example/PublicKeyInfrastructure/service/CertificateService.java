package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.dto.certificate.CertificateDTO;
import com.example.PublicKeyInfrastructure.dto.certificate.EECertificateDTO;
import com.example.PublicKeyInfrastructure.dto.certificate.IntermediateCertificateDTO;
import com.example.PublicKeyInfrastructure.dto.certificate.X509CertificateCreationDTO;
import com.example.PublicKeyInfrastructure.model.*;
import com.example.PublicKeyInfrastructure.repository.CertificateRepository;
import com.example.PublicKeyInfrastructure.utils.AESUtils;
import com.example.PublicKeyInfrastructure.utils.CertificateUtils;
import com.example.PublicKeyInfrastructure.utils.CertificateValidator;
import com.example.PublicKeyInfrastructure.utils.RSAUtils;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private RSAUtils rsaUtils;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private CertificateValidator certificateValidator;

    public Certificate createRootCertificate(CertificateDTO certificateDTO){

        Certificate certificate = setCertificateAttributes(certificateDTO, null);

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
            X509CertificateCreationDTO x509CertificateCreationDTO = new X509CertificateCreationDTO(certificateDTO);
            X509Certificate certificateX509 = certificateUtils.generateCertificate(x509CertificateCreationDTO, publicKey, privateKey, null, certificateDTO.getValidFrom(), certificateDTO.getValidTo(), true);
            String certificatePEM = certificateUtils.convertToPem(certificateX509);
            certificate.setPublicKeyPem(publicKeyPem);
            certificate.setCertificatePem(certificatePEM);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return certificateRepository.save(certificate);
    }

    public Certificate createIntermediateCertificate(IntermediateCertificateDTO intermediateCertificateDTO){
        Certificate issuerCertificate = findCertificateById(intermediateCertificateDTO.getIssuerCertificateId());
        certificateValidator.validateCertificateChain(issuerCertificate);
        Certificate certificate = setCertificateAttributes(intermediateCertificateDTO, issuerCertificate);
        PrivateKey issuerPrivateKey = null;
        if (issuerCertificate.getType() == CertificateType.ROOT){
            AdminMasterKey adminMasterKey = adminMasterKeyService.getMasterKey();
            String adminMasterKeyDecrypted = aesUtils.decrypt(adminMasterKey.getMasterKey());
            byte[] issuerPrivateKeyDecrypted = aesUtils.decryptPrivateKey(issuerCertificate.getPrivateKey(), adminMasterKeyDecrypted);
            issuerPrivateKey = rsaUtils.generatePrivateKey(issuerPrivateKeyDecrypted);
        }else{
            String issuerOrganizationMasterKeyEncrypted = issuerCertificate.getOrganization().getMasterKeyEncrypted();
            String issuerOrganizationMasterKeyDecrypted = aesUtils.decrypt(issuerOrganizationMasterKeyEncrypted);
            byte[] issuerPrivateKeyDecrypted = aesUtils.decryptPrivateKey(issuerCertificate.getPrivateKey(), issuerOrganizationMasterKeyDecrypted);
            issuerPrivateKey = rsaUtils.generatePrivateKey(issuerPrivateKeyDecrypted);
        }

        String organizationMasterKeyEncrypted = certificate.getOrganization().getMasterKeyEncrypted();
        String organizationMasterKey = aesUtils.decrypt(organizationMasterKeyEncrypted);

        try {
            KeyPair keyPair = certificateUtils.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                    Base64.getEncoder().encodeToString(publicKey.getEncoded()) +
                    "\n-----END PUBLIC KEY-----";
            PrivateKey privateKey = keyPair.getPrivate();
            String privateKeyEncrypted = aesUtils.encryptPrivateKey(privateKey, organizationMasterKey);
            certificate.setPrivateKey(privateKeyEncrypted);
            X509CertificateCreationDTO x509CertificateCreationDTO = new X509CertificateCreationDTO(intermediateCertificateDTO);
            X509Certificate certificateX509 = certificateUtils.generateCertificate(x509CertificateCreationDTO, publicKey, issuerPrivateKey, certificate.getIssuerData(), intermediateCertificateDTO.getValidFrom(), intermediateCertificateDTO.getValidTo(), true);
            String certificatePEM = certificateUtils.convertToPem(certificateX509);
            certificate.setPublicKeyPem(publicKeyPem);
            certificate.setCertificatePem(certificatePEM);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }



        return certificateRepository.save(certificate);
    }

    public Certificate createEndEntityCertificate(EECertificateDTO eecertificateDTO){
        Certificate issuerCertificate = findCertificateById(eecertificateDTO.getIssuerCertificateId());
        certificateValidator.validateCertificateChain(issuerCertificate);
        Certificate certificate = setCertificateAttributes(eecertificateDTO, issuerCertificate);
        PrivateKey issuerPrivateKey = null;
        if (issuerCertificate.getType() == CertificateType.ROOT){
            AdminMasterKey adminMasterKey = adminMasterKeyService.getMasterKey();
            String adminMasterKeyDecrypted = aesUtils.decrypt(adminMasterKey.getMasterKey());
            byte[] issuerPrivateKeyDecrypted = aesUtils.decryptPrivateKey(issuerCertificate.getPrivateKey(), adminMasterKeyDecrypted);
            issuerPrivateKey = rsaUtils.generatePrivateKey(issuerPrivateKeyDecrypted);
        }else{
            String issuerOrganizationMasterKeyEncrypted = issuerCertificate.getOrganization().getMasterKeyEncrypted();
            String issuerOrganizationMasterKeyDecrypted = aesUtils.decrypt(issuerOrganizationMasterKeyEncrypted);
            byte[] issuerPrivateKeyDecrypted = aesUtils.decryptPrivateKey(issuerCertificate.getPrivateKey(), issuerOrganizationMasterKeyDecrypted);
            issuerPrivateKey = rsaUtils.generatePrivateKey(issuerPrivateKeyDecrypted);
        }
        try {
            KeyPair keyPair = certificateUtils.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                    Base64.getEncoder().encodeToString(publicKey.getEncoded()) +
                    "\n-----END PUBLIC KEY-----";
            PrivateKey privateKey = keyPair.getPrivate();
            certificate.setPrivateKey(null);
            X509CertificateCreationDTO x509CertificateCreationDTO = new X509CertificateCreationDTO(eecertificateDTO);
            X509Certificate certificateX509 = certificateUtils.generateCertificate(x509CertificateCreationDTO, publicKey, issuerPrivateKey, certificate.getIssuerData(), eecertificateDTO.getValidFrom(), eecertificateDTO.getValidTo(), false);
            String certificatePEM = certificateUtils.convertToPem(certificateX509);
            certificate.setPublicKeyPem(publicKeyPem);
            certificate.setCertificatePem(certificatePEM);

            createPKCS12File(certificate, eecertificateDTO.getPasswordForCertificate(), privateKey);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return certificateRepository.save(certificate);

    }

    public Certificate findCertificateById(int id){
        return certificateRepository.findById(id).orElse(null);
    }

    private Certificate setCertificateAttributes(Object dto, Certificate issuerCertificate){
        Certificate certificate = new Certificate();
        if (dto instanceof CertificateDTO){
            CertificateDTO certificateDTO = (CertificateDTO) dto;
            certificate.setType(CertificateType.ROOT);
            certificate.setSerialNumber(certificateDTO.getSerialNumber());
            certificate.setSubjectCommonName(certificateDTO.getSubjectCommonName());
            certificate.setOrganization(null);
            AuthenticatedUser user = authenticatedUserService.findUserByEmail(certificateDTO.getSubjectEmail());
            certificate.setCAuser(user);
            certificate.setIssuerCertificate(null);
            certificate.setValidFrom(certificateDTO.getValidFrom());
            certificate.setValidTo(certificateDTO.getValidTo());
            certificate.setSubjectCommonName(certificateDTO.getSubjectCommonName());
            certificate.setSubjectOrganization(certificateDTO.getSubjectOrganizationName());
            certificate.setSubjectOrganizationalUnit(certificateDTO.getSubjectOrganizationalUnit());
            certificate.setSubjectEmail(certificateDTO.getSubjectEmail());
            certificate.setSubjectCountry(certificateDTO.getSubjectCountry());
            Map<String, String> issuerData = new HashMap<>();
            issuerData.put("nesto", "2");
            certificate.setIssuerData(issuerData);
            certificate.setCsrPem(null);
            certificate.setIsRevoked(false);
            certificate.setRevocationDate(null);
            certificate.setRevocationReason(null);
        }
        if (dto instanceof IntermediateCertificateDTO){
            IntermediateCertificateDTO intermediateCertificateDTO = (IntermediateCertificateDTO) dto;
            Map<String, String> issuerData = new HashMap<>();
            issuerData.put("SubjectCommonName", issuerCertificate.getSubjectCommonName());
            issuerData.put("SubjectOrganizationName", issuerCertificate.getSubjectOrganization());
            issuerData.put("SubjectOrganizationalUnit", issuerCertificate.getSubjectOrganizationalUnit());
            issuerData.put("SubjectCountry", issuerCertificate.getSubjectCountry());
            issuerData.put("SubjectEmail", issuerCertificate.getSubjectEmail());
            Organization organization = organizationService.findOrganizationByName(intermediateCertificateDTO.getSubjectOrganizationName());
            certificate.setOrganization(organization);
            certificate.setIssuerData(issuerData);
            certificate.setCsrPem(null);
            certificate.setIsRevoked(false);
            certificate.setRevocationDate(null);
            certificate.setRevocationReason(null);
            certificate.setType(CertificateType.INTERMEDIATE);
            AuthenticatedUser user = authenticatedUserService.findUserByEmail(intermediateCertificateDTO.getSubjectEmail());
            certificate.setCAuser(user);
            certificate.setIssuerCertificate(issuerCertificate);
            certificate.setValidFrom(intermediateCertificateDTO.getValidFrom());
            certificate.setValidTo(intermediateCertificateDTO.getValidTo());
            certificate.setSubjectCommonName(intermediateCertificateDTO.getSubjectCommonName());
            certificate.setSubjectOrganization(intermediateCertificateDTO.getSubjectOrganizationName());
            certificate.setSubjectOrganizationalUnit(intermediateCertificateDTO.getSubjectOrganizationalUnit());
            certificate.setSubjectEmail(intermediateCertificateDTO.getSubjectEmail());
            certificate.setSubjectCountry(intermediateCertificateDTO.getSubjectCountry());
            certificate.setSerialNumber(intermediateCertificateDTO.getSerialNumber());
        }
        if (dto instanceof EECertificateDTO){
            EECertificateDTO eecertificateDTO = (EECertificateDTO) dto;
            Map<String, String> issuerData = new HashMap<>();
            issuerData.put("SubjectCommonName", issuerCertificate.getSubjectCommonName());
            issuerData.put("SubjectOrganizationName", issuerCertificate.getSubjectOrganization());
            issuerData.put("SubjectOrganizationalUnit", issuerCertificate.getSubjectOrganizationalUnit());
            issuerData.put("SubjectCountry", issuerCertificate.getSubjectCountry());
            issuerData.put("SubjectEmail", issuerCertificate.getSubjectEmail());
            certificate.setOrganization(null);
            certificate.setIssuerData(issuerData);
            certificate.setCsrPem(null);
            certificate.setIsRevoked(false);
            certificate.setRevocationDate(null);
            certificate.setRevocationReason(null);
            certificate.setType(CertificateType.END_ENTITY);
            AuthenticatedUser user = authenticatedUserService.findUserByEmail(eecertificateDTO.getSubjectEmail());
            certificate.setCAuser(user);
            certificate.setIssuerCertificate(issuerCertificate);
            certificate.setValidFrom(eecertificateDTO.getValidFrom());
            certificate.setValidTo(eecertificateDTO.getValidTo());
            certificate.setSubjectCommonName(eecertificateDTO.getSubjectCommonName());
            certificate.setSubjectOrganization(null);
            certificate.setSubjectOrganizationalUnit(null);
            certificate.setSubjectEmail(eecertificateDTO.getSubjectEmail());
            certificate.setSubjectCountry(eecertificateDTO.getSubjectCountry());
            certificate.setSerialNumber(eecertificateDTO.getSerialNumber());

        }
        return certificate;
    }

    private void createPKCS12File(Certificate createdCertificate, String password, PrivateKey privateKey) {
        try {
            KeyStore pkcs12 = KeyStore.getInstance("PKCS12");
            List<X509Certificate> chainList = certificateUtils.createCertificateChain(createdCertificate);
            java.security.cert.Certificate[] chain = chainList.toArray(new java.security.cert.Certificate[0]);

            pkcs12.load(null, null);
            pkcs12.setKeyEntry("user-key", privateKey, password.toCharArray(), chain);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            pkcs12.store(baos, password.toCharArray());
            byte[] p12Bytes = baos.toByteArray();
            String userEmail = "nevenilincic@gmail.com";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart
            helper.setTo(userEmail);
            helper.setSubject("Vaš PKCS#12 sertifikat");
            helper.setText("U prilogu se nalazi vaš PKCS#12 sertifikat. Lozinka je: " + password);
            helper.addAttachment("user_cert.p12", new ByteArrayResource(p12Bytes));

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
