package com.example.PublicKeyInfrastructure.utils;

import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RSAUtils {


    public PrivateKey generatePrivateKey(byte[] decrypted){
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decrypted));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public PublicKey generatePublicKey(String publicKeyPem){
        try{
            String publicKeyContent = publicKeyPem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyContent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
