package com.example.PublicKeyInfrastructure.utils;

import com.example.PublicKeyInfrastructure.model.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;

@Component
public class CertificateValidator {

    @Autowired
    private CertificateUtils certificateUtils;
    @Autowired
    private RSAUtils rsaUtils;

    public boolean validateCertificateChain(Certificate issuerCertificate) {
        while (issuerCertificate.getIssuerCertificate() != null) {
            X509Certificate issuerX509Certificate = certificateUtils.pemToX509Certificate(issuerCertificate.getCertificatePem());
            Date now = new Date();
            if (issuerX509Certificate.getBasicConstraints() == -1) {
                throw new IllegalArgumentException("Certificate is not CA!");
            }
            if (now.before(issuerX509Certificate.getNotBefore()) || now.after(issuerX509Certificate.getNotAfter())) {
                throw new IllegalArgumentException("Certificate has expired!");
            }
            if (issuerCertificate.getIsRevoked()) {
                throw new IllegalArgumentException("Certificate is revoked!");
            }
            PublicKey issuerPublicKey = null;
            if (issuerCertificate.getIssuerCertificate() == null) {
//            issuerPublicKey = issuerX509Certificate.getPublicKey();
                issuerPublicKey = rsaUtils.generatePublicKey(issuerCertificate.getPublicKeyPem());
            } else {
                issuerPublicKey = rsaUtils.generatePublicKey(issuerCertificate.getIssuerCertificate().getPublicKeyPem());
            }
            try {
                issuerX509Certificate.verify(issuerPublicKey);
            } catch (Exception e) {
                throw new IllegalArgumentException("Digital sign is invalid!");
            }

            System.out.println("SERTIFIKAT VALIDAN ID: " + issuerCertificate.getId().toString());
            issuerCertificate = issuerCertificate.getIssuerCertificate();
        }

        return true;
    }
}
