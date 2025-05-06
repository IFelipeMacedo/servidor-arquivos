package com.criptografia.servidor_arquivos.crypto;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class DiffieHellmanUtil {

    static {
        Security.addProvider(new BouncyCastleProvider()); // Adiciona o provedor BouncyCastle
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH", "BC");
        keyPairGenerator.initialize(512); // Tamanho da chave
        return keyPairGenerator.generateKeyPair();
    }

    public static SecretKey generateSharedSecret(PrivateKey privateKey, PublicKey publicKey) throws Exception {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH", "BC");
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey, true);
        return keyAgreement.generateSecret("AES");
    }

    public class ECDiffieHellmanUtil {
        public static SecretKey generateSharedSecretEC(PrivateKey privateKey, PublicKey publicKey) throws Exception {
            KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(publicKey, true);
            byte[] sharedSecret = keyAgreement.generateSecret();

            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = sha256.digest(sharedSecret);
            return new SecretKeySpec(keyBytes, 0, 16, "AES");
        }
    }

}

