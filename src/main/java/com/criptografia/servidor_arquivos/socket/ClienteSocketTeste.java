package com.criptografia.servidor_arquivos.socket;

import com.criptografia.servidor_arquivos.crypto.DiffieHellmanUtil;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;

public class ClienteSocketTeste {

    public static void main(String[] args) {
        try {
            // Conecta ao servidor
            Socket socket = new Socket("localhost", 12345);
            System.out.println("Conectado ao servidor.");

            // Recebe chave pública do servidor
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            PublicKey serverPublicKey = (PublicKey) in.readObject();
            System.out.println("Chave pública do servidor recebida.");

            // Gera par de chaves do cliente
            KeyPair keyPair = DiffieHellmanUtil.generateKeyPair();

            // Envia chave pública do cliente
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(keyPair.getPublic());
            out.flush();
            System.out.println("Chave pública enviada.");

            // Gera chave secreta compartilhada
            SecretKey sharedSecret = DiffieHellmanUtil.generateSharedSecret(keyPair.getPrivate(), serverPublicKey);
            System.out.println("Chave secreta compartilhada gerada no cliente.");

            // Aqui você pode usar a chave compartilhada para criptografar mensagens futuras
            socket.close();
            System.out.println("Conexão encerrada.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
