package com.criptografia.servidor_arquivos.controller;

import com.criptografia.servidor_arquivos.model.File;
import com.criptografia.servidor_arquivos.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao enviar arquivo");
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long id,
                                                          @RequestParam("algorithm") String algorithm) {
        try {
            File fileEntity = fileService.getFileEntity(id);
            byte[] decryptedData = fileService.decryptFile(id, algorithm);
            ByteArrayResource resource = new ByteArrayResource(decryptedData);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileEntity.getFiletype()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/list")
    public List<File> listFiles() {
        return fileService.listFiles();
    }
}
