package com.criptografia.servidor_arquivos.repository;

import com.criptografia.servidor_arquivos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}