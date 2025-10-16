package com.example.PublicKeyInfrastructure.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Component
public class AESUtil {

    public String encrypt(String message){
        try {
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = getSecretKey();
            byte[] ivBytes = generateIV();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

            Cipher aesCipherEnc = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCipherEnc.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            //sifrovanje
            byte[] ciphertext = aesCipherEnc.doFinal(messageBytes);

            byte[] combined = new byte[ivBytes.length + ciphertext.length];
            System.arraycopy(ivBytes, 0, combined, 0, ivBytes.length);
            System.arraycopy(ciphertext, 0, combined, ivBytes.length, ciphertext.length);

            String encodedCiphertext = Base64.getUrlEncoder().withoutPadding().encodeToString(combined);

            return encodedCiphertext;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String decrypt(String encryptedBase64){
        try {
            byte[] combined = Base64.getUrlDecoder().decode(encryptedBase64);

            // Prvih 16 bajtova je IV
            byte[] iv = Arrays.copyOfRange(combined, 0, 16);
            byte[] encrypted = Arrays.copyOfRange(combined, 16, combined.length);
            System.out.println(combined.length);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey secretKey = getSecretKey();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            //dekriptovanje
            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private SecretKey getSecretKey(){
        String secretKey = System.getenv("SECRET_KEY");
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKeyAES = new SecretKeySpec(keyBytes, "AES");
        return secretKeyAES;
    }

    private byte[] generateIV() {
        byte []iv = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return iv;
    }
}
