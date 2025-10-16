package com.example.PublicKeyInfrastructure.utils;

import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

@Component
public class RSAUtils {


    public PrivateKey generatePrivateKey(byte[] decrypted, String algorithm){
        try{
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decrypted));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
