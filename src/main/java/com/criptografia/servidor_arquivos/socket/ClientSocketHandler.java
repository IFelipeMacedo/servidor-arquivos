package com.criptografia.servidor_arquivos.socket;

import com.criptografia.servidor_arquivos.crypto.DiffieHellmanUtil;

import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.SecretKey;

public class ClientSocketHandler {

    private Socket socket;

    public void connectToServer() throws Exception {
        socket = new Socket("localhost", 12345);
        System.out.println("Conectado ao servidor...");

        KeyPair keyPair = DiffieHellmanUtil.generateKeyPair();
        PublicKey serverPublicKey = receiveServerPublicKey();
        SecretKey sharedSecret = DiffieHellmanUtil.generateSharedSecret(keyPair.getPrivate(), serverPublicKey);


        sendPublicKey(keyPair.getPublic());


        communicateWithServer(sharedSecret);
    }

    private PublicKey receiveServerPublicKey() throws IOException {
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        try {
            return (PublicKey) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Erro ao receber chave pública", e);
        }
    }

    private void sendPublicKey(PublicKey publicKey) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(publicKey);
    }

    private void communicateWithServer(SecretKey sharedSecret) throws IOException {
    }
}
