package com.criptografia.servidor_arquivos.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionUtil {

    public static String md5Hash(String input) throws NoSuchAlgorithmException {
        return hash(input, "MD5");
    }

    public static String sha256Hash(String input) throws NoSuchAlgorithmException {
        return hash(input, "SHA-256");
    }

    private static String hash(String input, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = digest.digest(input.getBytes());
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}

