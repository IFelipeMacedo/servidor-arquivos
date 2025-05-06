package com.criptografia.servidor_arquivos.controller;

import com.criptografia.servidor_arquivos.model.File;
import com.criptografia.servidor_arquivos.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("algorithm") String algorithm,
                                             @RequestParam("email") String email) {
        try {
            fileService.uploadFile(file, algorithm, email);
            return ResponseEntity.ok("Arquivo enviado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao enviar arquivo");
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id,
                                               @RequestParam("algorithm") String algorithm) {
        try {
            byte[] fileData = fileService.downloadFile(id, algorithm);
            return ResponseEntity.ok(fileData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/list")
    public List<File> listFiles() {
        return fileService.listFiles();
    }
}
