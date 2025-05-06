package com.criptografia.servidor_arquivos.service;

import com.criptografia.servidor_arquivos.model.EncryptedFile;
import com.criptografia.servidor_arquivos.model.File;
import com.criptografia.servidor_arquivos.model.User;
import com.criptografia.servidor_arquivos.repository.FileRepository;
import com.criptografia.servidor_arquivos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CryptoService cryptoService;

    public void uploadFile(MultipartFile file, String algorithm, String email) throws Exception {
        byte[] fileData = file.getBytes();

        EncryptedFile encryptedFile = cryptoService.encryptFile(fileData, algorithm);

        User uploader = userRepository.findByEmail(email);
        if (uploader == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário com email '" + email + "' não encontrado");
        }

        File fileEntity = new File();
        fileEntity.setFilename(file.getOriginalFilename());
        fileEntity.setFiletype(file.getContentType());
        fileEntity.setData(encryptedFile.getData());
        fileEntity.setEncryptionKeyBase64(cryptoService.encodeKey(encryptedFile.getKey()));
        fileEntity.setAlgorithmUsed(algorithm);
        fileEntity.setUploadTimestamp(LocalDateTime.now().toString());
        fileEntity.setUploader(uploader);

        fileRepository.save(fileEntity);
    }

    public byte[] decryptFile(Long fileId, String algorithm) throws Exception {
        File file = getFileEntity(fileId);
        byte[] keyBytes = cryptoService.decodeKey(file.getEncryptionKeyBase64());
        return cryptoService.decryptFile(file.getData(), algorithm, keyBytes);
    }

    public File getFileEntity(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));
    }

    public List<File> listFiles() {
        return fileRepository.findAll();
    }
}
