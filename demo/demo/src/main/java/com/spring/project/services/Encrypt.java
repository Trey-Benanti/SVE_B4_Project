package com.spring.project.services;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * POJO for encrypting and decrypting data.
 */
public class Encrypt {

    private Cipher cipher;

    public Encrypt() throws Exception {
        this.cipher = Cipher.getInstance("AES");
    }

    public String encrypt(String toEncrypt, SecretKey secretKey) throws Exception {
        byte[] toEncryptByte = toEncrypt.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encByte = cipher.doFinal(toEncryptByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encString = encoder.encodeToString(encByte);
        return encString;
    } // encrypt

    public String decrypt(String toDecrypt, SecretKey secretKey) throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encByte = decoder.decode(toDecrypt);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decByte = cipher.doFinal(encByte);
        String decString = new String(decByte);
        return decString;
    } // decrypt

} // Encrypt
