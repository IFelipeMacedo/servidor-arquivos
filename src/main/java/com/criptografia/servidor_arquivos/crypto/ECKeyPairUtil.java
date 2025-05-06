package com.criptografia.servidor_arquivos.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ECKeyPairUtil {

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256); // EC com curva de 256 bits
        return keyPairGenerator.generateKeyPair();
    }

    public static PublicKey getPublicKey(KeyPair keyPair) {
        return keyPair.getPublic();
    }

    public static PrivateKey getPrivateKey(KeyPair keyPair) {
        return keyPair.getPrivate();
    }
}

