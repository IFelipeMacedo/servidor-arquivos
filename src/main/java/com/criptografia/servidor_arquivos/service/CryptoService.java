package com.criptografia.servidor_arquivos.service;

import com.criptografia.servidor_arquivos.model.EncryptedFile;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CryptoService {

    public EncryptedFile encryptFile(byte[] data, String algorithm) throws Exception {
        SecretKey key = generateSecretKey(algorithm);
        byte[] encryptedData = process(true, data, key.getEncoded(), algorithm);
        return new EncryptedFile(encryptedData, key);
    }

    public byte[] decryptFile(byte[] encryptedData, String algorithm, byte[] keyBytes) throws Exception {
        return process(false, encryptedData, keyBytes, algorithm);
    }

    private byte[] process(boolean forEncryption, byte[] input, byte[] keyBytes, String algorithm) throws Exception {
        PaddedBufferedBlockCipher cipher = getCipher(algorithm);
        cipher.init(forEncryption, new KeyParameter(keyBytes));

        byte[] output = new byte[cipher.getOutputSize(input.length)];
        int bytesProcessed = cipher.processBytes(input, 0, input.length, output, 0);
        bytesProcessed += cipher.doFinal(output, bytesProcessed);

        byte[] finalOutput = new byte[bytesProcessed];
        System.arraycopy(output, 0, finalOutput, 0, bytesProcessed);

        return finalOutput;
    }

    private PaddedBufferedBlockCipher getCipher(String algorithm) {
        switch (algorithm.toUpperCase()) {
            case "AES":
                return new PaddedBufferedBlockCipher(new AESEngine(), new PKCS7Padding());
            case "DES":
                return new PaddedBufferedBlockCipher(new DESEngine(), new PKCS7Padding());
            case "3DES":
            case "DES3":
            case "DESEDE":
                return new PaddedBufferedBlockCipher(new DESedeEngine(), new PKCS7Padding());
            default:
                throw new IllegalArgumentException("Algoritmo não suportado: " + algorithm);
        }
    }

    public SecretKey generateSecretKey(String algorithm) throws Exception {
        KeyGenerator keyGen;
        switch (algorithm.toUpperCase()) {
            case "AES":
                keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(128);
                break;
            case "DES":
                keyGen = KeyGenerator.getInstance("DES");
                keyGen.init(56);
                break;
            case "3DES":
            case "DES3":
            case "DESEDE":
                keyGen = KeyGenerator.getInstance("DESede");
                keyGen.init(168);
                break;
            default:
                throw new IllegalArgumentException("Algoritmo não suportado");
        }
        return keyGen.generateKey();
    }

    public String encodeKey(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public byte[] decodeKey(String base64Key) {
        return Base64.getDecoder().decode(base64Key);
    }
}


