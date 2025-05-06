package com.criptografia.servidor_arquivos.controller;

import com.criptografia.servidor_arquivos.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        if (authService.authenticate(username, password)) {
            return ResponseEntity.ok("Autenticação bem-sucedida");
        } else {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username, @RequestParam String password) {
        authService.registerUser(username, password);
        return ResponseEntity.ok("Usuário registrado com sucesso");
    }
}

