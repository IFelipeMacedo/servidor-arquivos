package com.criptografia.servidor_arquivos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncryptedFile {
    private byte[] data;
    private SecretKey key;
}
