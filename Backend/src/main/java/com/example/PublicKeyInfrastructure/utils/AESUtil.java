package com.example.PublicKeyInfrastructure.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AESUtil {

    public String encrypt(String message){
        try {
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            String secretKey = System.getenv("SECRET_KEY");
            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            SecretKey secretKeyAES = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec iv = generateIV();
            Cipher aesCipherEnc = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCipherEnc.init(Cipher.ENCRYPT_MODE, secretKeyAES, iv);

            //sifrovanje
            byte[] ciphertext = aesCipherEnc.doFinal(messageBytes);
            String encodedCiphertext = Base64.getEncoder().encodeToString(ciphertext);

            return encodedCiphertext;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private IvParameterSpec generateIV() {
        byte []iv = new byte[16];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        return ivParameterSpec;
    }
}
