package com.criptografia.servidor_arquivos.service;

import com.criptografia.servidor_arquivos.model.User;
import com.criptografia.servidor_arquivos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean authenticate(String username, String password) {
        User user = userRepository.findByEmail(username);
        if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
            return true;
        }
        return false;
    }

    public void registerUser(String username, String password) {
        String passwordHash = passwordEncoder.encode(password);
        User user = new User();
        user.setEmail(username);
        user.setPasswordHash(passwordHash);
        userRepository.save(user);
    }
}

