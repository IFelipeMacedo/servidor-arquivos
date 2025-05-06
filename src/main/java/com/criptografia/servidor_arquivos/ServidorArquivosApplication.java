package com.criptografia.servidor_arquivos;

import com.criptografia.servidor_arquivos.socket.ServerSocketHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServidorArquivosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServidorArquivosApplication.class, args);
	}

	@Bean
	public CommandLineRunner runSocketServer() {
		return args -> {
			ServerSocketHandler socketServer = new ServerSocketHandler();
			new Thread(() -> {
				try {
					socketServer.startServer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		};
	}
}
