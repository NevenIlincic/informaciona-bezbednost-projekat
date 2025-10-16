package com.example.PublicKeyInfrastructure.utils;

import com.example.PublicKeyInfrastructure.dto.certificate.CertificateDTO;
import com.example.PublicKeyInfrastructure.model.Certificate;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
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
import java.util.Base64;
import java.util.Date;

@Component
public class CertificateUtils {

    @Autowired
    private DateUtils dateUtils;
    @Autowired
    private RSAUtils rsaUtils;

    public CertificateUtils() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public X509Certificate generateCertificate(CertificateDTO certificateDTO){
        try {
            KeyPair generatedKeys = generateKeyPair();
            X500Name subject = new X500NameBuilder(BCStyle.INSTANCE)
                    .addRDN(BCStyle.CN, certificateDTO.getSubjectCommonName())
                    .addRDN(BCStyle.O, certificateDTO.getSubjectOrganizationName())
                    .addRDN(BCStyle.OU, certificateDTO.getSubjectOrganizationalUnit())
                    .addRDN(BCStyle.C, certificateDTO.getSubjectCountry())
                    .addRDN(BCStyle.E, certificateDTO.getSubjectEmail())
                    .build();
//            X509Certificate issuerCertificate = pemToX509Certificate(pem);
//            X500Name issuer = new JcaX509CertificateHolder(issuerCertificate).getIssuer();

            Date notBefore = dateUtils.convertToDate(10, 2, 2025);
            Date notAfter = dateUtils.convertToDate(10, 12, 2025);
            X509Certificate certificate = createCertificate(
                    generatedKeys,
                    generatedKeys.getPrivate(),
                    subject,
                    subject,
                    notBefore,
                    notAfter,
                    new BigInteger(certificateDTO.getSerialNumber())


            );
            return certificate;
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    // Generiše par ključeva za sertifikat
    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    // Kreiranje X.509 sertifikata
    private X509Certificate createCertificate(KeyPair certKeyPair,
                                                    PrivateKey issuerPrivateKey,
                                                    X500Name subject,
                                                    X500Name issuer,
                                                    Date notBefore,
                                                    Date notAfter,
                                                    BigInteger serialNumber) throws Exception {

        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuer,
                serialNumber,
                notBefore,
                notAfter,
                subject,
                certKeyPair.getPublic()
        );

        // Potpisivanje sertifikata privatnim ključem izdavaoca
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
                .build(issuerPrivateKey);

        return new JcaX509CertificateConverter()
                .setProvider("BC")
                .getCertificate(certBuilder.build(signer));
    }

    public X509Certificate pemToX509Certificate(String pem) throws Exception {
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
}
