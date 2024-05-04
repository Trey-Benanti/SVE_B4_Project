package com.spring.project.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.spring.project.models.users.User;
import com.spring.project.models.users.userinfo.CardInfo;

/**
 * POJO for encrypting and decrypting data.
 */
public final class EncryptFacade {

    // EncryptFacade instance
    private static EncryptFacade INSTANCE; 

    // Password Encryption
    private BCryptPasswordEncoder passwordEncoder;

    // Card Encryption
    private Cipher cipher;
    private File keystoreFile = new File("keystore.jceks");
    private KeyGenerator keyGenerator;
    private KeyStore keyStore;
    private char[] ksPassword;

    public EncryptFacade() throws Exception {
        this.passwordEncoder = new BCryptPasswordEncoder();
        
        this.cipher = Cipher.getInstance("AES");
        this.keyGenerator = KeyGenerator.getInstance("AES"); // Generate secret key
        this.keyGenerator.init(128);
        this.keyStore = KeyStore.getInstance("JCEKS");
        this.ksPassword = "password".toCharArray();
    }

    // getInstance of Encryptor
    public static EncryptFacade getInstance() {
        if(INSTANCE == null) {
            try{
                INSTANCE = new EncryptFacade();
            } catch (Exception g) {
                System.out.println(g);
            }
        }
        return INSTANCE;
    }

    // password encryption methods

    public void encryptPassword(User user, String password) {
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);        
    } // encryptPassword

    public boolean compareToPassword(User user, String passToCheck) {
        boolean matches = passwordEncoder.matches(passToCheck, user.getPassword());
        return matches;
    }

    // card encryption methods

    public String encryptCard(User user, String cardInfo, int cardId) throws Exception {
        SecretKey secretKey = keyGenerator.generateKey();

        loadKeyStore();
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey); // Set keystore entry params
        KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(ksPassword);
        this.keyStore.setEntry(user.getPaymentInfo().get(cardId).getId().toString(), secretKeyEntry, entryPassword);
    
        try(FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            keyStore.store(fos, ksPassword);
        } // try
        byte[] toEncryptByte = cardInfo.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encByte = cipher.doFinal(toEncryptByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encString = encoder.encodeToString(encByte);
        return encString;
    } // encrypt

    public String decryptCard(CardInfo card) throws Exception {
        loadKeyStore();

        KeyStore.ProtectionParameter protPassword = new KeyStore.PasswordProtection(ksPassword); // Retrieve key from keystore
        KeyStore.SecretKeyEntry tempKey = (KeyStore.SecretKeyEntry) keyStore.getEntry(card.getId().toString(), protPassword);
        SecretKey secretKey = new SecretKeySpec(tempKey.getSecretKey().getEncoded(), "AES");

        String toDecrypt = card.getCardNumber();

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encByte = decoder.decode(toDecrypt);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decByte = cipher.doFinal(encByte);
        String decString = new String(decByte);
        return decString;
    } // decrypt

    private void loadKeyStore(){
        try { // DON'T CHANGE THIS OMG >:(
            try(FileInputStream fis = new FileInputStream(keystoreFile)) {
                this.keyStore.load(fis, ksPassword);
            }
        } catch (IOException f) {
            System.out.println(f);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        } catch (CertificateException d) {
            System.out.println(d);
        } // try catch
    } // loadKeyStore

} // Encrypt
