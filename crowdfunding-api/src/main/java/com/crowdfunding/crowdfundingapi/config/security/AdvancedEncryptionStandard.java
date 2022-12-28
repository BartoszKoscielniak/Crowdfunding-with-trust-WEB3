package com.crowdfunding.crowdfundingapi.config.security;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
@AllArgsConstructor
public class AdvancedEncryptionStandard {

    public static final int GCM_TAG_LENGTH = 16;
    private static final String ALGO = "AES/GCM/NoPadding";
    private static final String SALT = "LCJhdXRiNXIdGllc";
    private static final byte[] IV = new byte[] {23, 17, -96, -93, 2, -58, 43, -107, 68, 116, -84, -44, -80, -113, -18, -108};
    private static final String PASSWORD = "VhOURiNzA3YzQwYTZFNTAyQThBRTY2MTNmZmMyMjJiMzBDM0YiLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoidXNlcjpjcmVhdGUifSx7ImF1dGhvcml0eSI6InVzZXI6ZGVsZXRlIn0seyJhdXRob3JpdHkiOiJST0xFX1VTRVIifSx7ImF1dGhvcml0eSI6InVzZXI6cmVhZCJ9LHsiYXV0aG9yaXR5IjoidXNlcjp1cGRhdGUifV0sImlhdCI6MTY3MTc1MTY4MiwiZXhwIjoxNjcyMzU0ODAwfQ";

    private static SecretKey keyFromPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySpec = new PBEKeySpec(PASSWORD.toCharArray(), SALT.getBytes(), 65536, 256);
        return new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), "AES");
    }

    private static byte[] ivParameterSpec( ) {
        byte[] parameter = new byte[GCM_TAG_LENGTH];
        new SecureRandom().nextBytes(parameter);
        return parameter;
    }

    public static byte[] encrypt(byte[] plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(keyFromPassword().getEncoded(), "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, AdvancedEncryptionStandard.IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
        return cipher.doFinal(plaintext);
    }

    public static String decrypt(byte[] cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(keyFromPassword().getEncoded(), "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, AdvancedEncryptionStandard.IV);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
        return new String(cipher.doFinal(cipherText));
    }
}
