package com.example.PublicKeyInfrastructure.utils;

import com.example.PublicKeyInfrastructure.dto.certificate.CertificateDTO;
import com.example.PublicKeyInfrastructure.dto.certificate.X509CertificateCreationDTO;
import com.example.PublicKeyInfrastructure.model.Certificate;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class CertificateUtils {

    @Autowired
    private DateUtils dateUtils;
    @Autowired
    private RSAUtils rsaUtils;

    public CertificateUtils() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public X509Certificate generateCertificate(X509CertificateCreationDTO certificateDTO, PublicKey publicKey, PrivateKey issuerPrivateKey, Map<String, String> issuerData, LocalDateTime validFrom, LocalDateTime validTo, boolean isCACertificate){
        try {
            X500Name subject = new X500NameBuilder(BCStyle.INSTANCE)
                    .addRDN(BCStyle.CN, certificateDTO.getSubjectCommonName())
                    .addRDN(BCStyle.O, certificateDTO.getSubjectOrganizationName())
                    .addRDN(BCStyle.OU, certificateDTO.getSubjectOrganizationalUnit())
                    .addRDN(BCStyle.C, certificateDTO.getSubjectCountry())
                    .addRDN(BCStyle.E, certificateDTO.getSubjectEmail())
                    .build();
            X500Name issuer;
            if (issuerData == null) {
                issuer = subject;
            }else{
                issuer = new X500NameBuilder(BCStyle.INSTANCE)
                        .addRDN(BCStyle.CN, issuerData.get("SubjectCommonName"))
                        .addRDN(BCStyle.O, issuerData.get("SubjectOrganizationName"))
                        .addRDN(BCStyle.OU, issuerData.get("SubjectOrganizationalUnit"))
                        .addRDN(BCStyle.C, issuerData.get("SubjectCountry"))
                        .addRDN(BCStyle.E, issuerData.get("SubjectEmail"))
                        .build();
            }

            Date notBefore = dateUtils.convertToDate(validFrom.getDayOfMonth(), validFrom.getMonthValue(), validFrom.getYear());
            Date notAfter = dateUtils.convertToDate(validTo.getDayOfMonth(), validTo.getMonthValue(), validTo.getYear());
            X509Certificate certificate = createCertificate(
                    publicKey,
                    issuerPrivateKey,
                    subject,
                    issuer,
                    notBefore,
                    notAfter,
                    certificateDTO.getSerialNumber(),
                    isCACertificate


            );
            return certificate;
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    // Generiše par ključeva za sertifikat
    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    // Kreiranje X.509 sertifikata
    private X509Certificate createCertificate(PublicKey publicKey,
                                                    PrivateKey issuerPrivateKey,
                                                    X500Name subject,
                                                    X500Name issuer,
                                                    Date notBefore,
                                                    Date notAfter,
                                                    BigInteger serialNumber,
                                              boolean isCACertificate) throws Exception {

        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuer,
                serialNumber,
                notBefore,
                notAfter,
                subject,
                publicKey
        );

        // Dodavanje ekstenzija
        certBuilder.addExtension(
                Extension.basicConstraints,
                false, // critical
                new BasicConstraints(isCACertificate) // true = CA, false = end-entity
        );

        certBuilder.addExtension(
                Extension.keyUsage,
                false,
                new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment)
        );

        certBuilder.addExtension(
                Extension.extendedKeyUsage,
                false,
                new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth)
        );

        // Potpisivanje sertifikata privatnim ključem izdavaoca
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
                .build(issuerPrivateKey);

        return new JcaX509CertificateConverter()
                .setProvider("BC")
                .getCertificate(certBuilder.build(signer));
    }

    public X509Certificate pemToX509Certificate(String pem) {
        try {
            // Ukloni zaglavlja i razmake iz PEM-a
            String sanitized = pem
                    .replace("-----BEGIN CERTIFICATE-----", "")
                    .replace("-----END CERTIFICATE-----", "")
                    .replaceAll("\\s", "");

            // Dekoduj Base64
            byte[] certBytes = Base64.getDecoder().decode(sanitized);

            // Napravi CertificateFactory i generiši X509Certificate
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String convertToPem(X509Certificate certificate) throws Exception {
        StringBuilder pemBuilder = new StringBuilder();
        pemBuilder.append("-----BEGIN CERTIFICATE-----\n");

        // Enkoduj sertifikat u Base64
        String encoded = Base64.getEncoder().encodeToString(certificate.getEncoded());

        // Dodaj linije od po 64 karaktera (standard PEM format)
        int index = 0;
        while (index < encoded.length()) {
            int endIndex = Math.min(index + 64, encoded.length());
            pemBuilder.append(encoded, index, endIndex);
            pemBuilder.append("\n");
            index = endIndex;
        }

        pemBuilder.append("-----END CERTIFICATE-----\n");
        return pemBuilder.toString();
    }

    public List<X509Certificate> createCertificateChain(Certificate createdCertficate){
        List<X509Certificate> certList = new ArrayList<>();
        while (createdCertficate != null) {
            System.out.println(createdCertficate.getType().toString());
            X509Certificate createdX509Certificate = pemToX509Certificate(createdCertficate.getCertificatePem());
            certList.add(createdX509Certificate);
            createdCertficate = createdCertficate.getIssuerCertificate();
        }
        return certList;

    }
}
