package com.criptografia.servidor_arquivos.socket;

import com.criptografia.servidor_arquivos.crypto.DiffieHellmanUtil;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;

public class ServerSocketHandler {

    private ServerSocket serverSocket;

    public void startServer() throws Exception {
        serverSocket = new ServerSocket(12345);
        System.out.println("Servidor iniciado...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado!");

            // Gera par de chaves do servidor
            KeyPair keyPair = DiffieHellmanUtil.generateKeyPair();

            // Envia chave pública do servidor
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeObject(keyPair.getPublic());
            outputStream.flush();

            // Recebe chave pública do cliente
            PublicKey clientPublicKey = receiveClientPublicKey(clientSocket);

            // Gera chave secreta compartilhada
            SecretKey sharedSecret = DiffieHellmanUtil.generateSharedSecret(keyPair.getPrivate(), clientPublicKey);
            System.out.println("Chave secreta compartilhada gerada no servidor.");

            // Aqui você pode continuar a comunicação criptografada com o cliente
            clientSocket.close();
        }
    }

    private PublicKey receiveClientPublicKey(Socket clientSocket) throws IOException {
        ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
        try {
            return (PublicKey) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Erro ao receber chave pública do cliente", e);
        }
    }
}
