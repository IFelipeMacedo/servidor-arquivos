package com.criptografia.servidor_arquivos.service;

import com.criptografia.servidor_arquivos.model.EncryptedFile;
import com.criptografia.servidor_arquivos.crypto.DiffieHellmanUtil;
import com.criptografia.servidor_arquivos.crypto.ECKeyPairUtil;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Service
public class CryptoService {

    public EncryptedFile encryptFile(byte[] data, String algorithm) throws Exception {
        SecretKey key = generateSharedSecretKey(algorithm);
        byte[] encryptedData = process(true, data, key.getEncoded(), algorithm);
        return new EncryptedFile(encryptedData, key);
    }

    public byte[] decryptFile(byte[] encryptedData, String algorithm, byte[] keyBytes) throws Exception {
        return process(false, encryptedData, keyBytes, algorithm);
    }

    private byte[] process(boolean forEncryption, byte[] input, byte[] keyBytes, String algorithm) throws Exception {
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new AESEngine(), new PKCS7Padding());
        cipher.init(forEncryption, new KeyParameter(keyBytes));

        byte[] output = new byte[cipher.getOutputSize(input.length)];
        int bytesProcessed = cipher.processBytes(input, 0, input.length, output, 0);
        bytesProcessed += cipher.doFinal(output, bytesProcessed);

        byte[] finalOutput = new byte[bytesProcessed];
        System.arraycopy(output, 0, finalOutput, 0, bytesProcessed);

        return finalOutput;
    }

    public SecretKey generateSharedSecretKey(String algorithm) throws Exception {
        switch (algorithm.toUpperCase()) {
            case "DH":
                return DiffieHellmanUtil.generateSharedSecret(
                        DiffieHellmanUtil.generateKeyPair().getPrivate(),
                        DiffieHellmanUtil.generateKeyPair().getPublic());
            case "EC":
                KeyPair ecKeyPair = ECKeyPairUtil.generateKeyPair();
                return DiffieHellmanUtil.ECDiffieHellmanUtil.generateSharedSecretEC(ecKeyPair.getPrivate(), ecKeyPair.getPublic());


            case "AES":
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(256);  // Pode alterar o tamanho da chave conforme necessário
                return keyGenerator.generateKey();

            default:
                throw new IllegalArgumentException("Algoritmo não suportado");
        }
    }

    private SecretKey generateSharedSecret(PrivateKey privateKey, PublicKey publicKey) throws Exception {
        return DiffieHellmanUtil.generateSharedSecret(privateKey, publicKey);
    }

    public String encodeKey(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public byte[] decodeKey(String encodedKey) {
        return Base64.getDecoder().decode(encodedKey);
    }
}
