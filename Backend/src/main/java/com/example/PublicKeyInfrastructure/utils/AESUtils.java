package com.example.PublicKeyInfrastructure.utils;

import com.example.PublicKeyInfrastructure.model.AdminMasterKey;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

@Component
public class AESUtils {

    private final int CBC_IV_LENGHT = 16;
    private final int GCM_IV_LENGTH = 12;   // 12 bajtova IV
    private final int GCM_TAG_LENGTH = 128;

    public String encrypt(String message){
        try {
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            String key = System.getenv("SECRET_KEY");
            SecretKey secretKey = getSecretKey(key);
            byte[] ivBytes = generateIV(CBC_IV_LENGHT);
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
            byte[] iv = Arrays.copyOfRange(combined, 0, CBC_IV_LENGHT);
            byte[] encrypted = Arrays.copyOfRange(combined, CBC_IV_LENGHT, combined.length);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            String key = System.getenv("SECRET_KEY");
            SecretKey secretKey = getSecretKey(key);
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
    public String encryptPrivateKey(PrivateKey privateKey, String masterKey) {
        try {
            byte[] keyBytes = privateKey.getEncoded();

            SecretKey secretKey = getSecretKey(masterKey);

            byte[] iv =  generateIV(GCM_IV_LENGTH);

            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

            byte[] encrypted = cipher.doFinal(keyBytes);

            // kombinuj IV + ciphertext
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getUrlEncoder().withoutPadding().encodeToString(combined);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Dešifrovanje privatnog ključa master ključem
    public byte[] decryptPrivateKey(String encryptedBase64, String masterKey) {
        try {
            byte[] combined = Base64.getUrlDecoder().decode(encryptedBase64);

            byte[] iv = Arrays.copyOfRange(combined, 0, GCM_IV_LENGTH);
            byte[] encrypted = Arrays.copyOfRange(combined, GCM_IV_LENGTH, combined.length);

            SecretKey secretKey = getSecretKey(masterKey);


            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            byte[] decrypted = cipher.doFinal(encrypted);
            return decrypted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private SecretKey getSecretKey(String key){
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] keyHash = sha.digest(key.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(keyHash, "AES");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] generateIV(int numBytes) {
        byte []iv = new byte[numBytes];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return iv;
    }

    public String generateMasterKey(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            SecretKey masterKey = keyGenerator.generateKey();
            String masterKeyBase64 = Base64.getEncoder().encodeToString(masterKey.getEncoded());
            return masterKeyBase64;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
