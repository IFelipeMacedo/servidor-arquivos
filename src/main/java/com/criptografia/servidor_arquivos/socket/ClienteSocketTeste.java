package com.criptografia.servidor_arquivos.socket;

import com.criptografia.servidor_arquivos.crypto.DiffieHellmanUtil;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Base64;

public class ClienteSocketTeste {

    public static void main(String[] args) {
        try {

            Socket socket = new Socket("localhost", 12345);
            System.out.println("Conectado ao servidor.");


            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            PublicKey serverPublicKey = (PublicKey) in.readObject();
            System.out.println(serverPublicKey);
            System.out.println("Chave pública do servidor recebida.");


            KeyPair keyPair = DiffieHellmanUtil.generateKeyPair();


            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(keyPair.getPublic());
            out.flush();
            System.out.println("Chave pública enviada.");


            SecretKey sharedSecret = DiffieHellmanUtil.generateSharedSecret(keyPair.getPrivate(), serverPublicKey);
            String encodedKey = Base64.getEncoder().encodeToString(sharedSecret.getEncoded());
            System.out.println("Chave secreta (Base64): " + encodedKey);
            System.out.println("Chave secreta compartilhada gerada no cliente.");


            socket.close();
            System.out.println("Conexão encerrada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
