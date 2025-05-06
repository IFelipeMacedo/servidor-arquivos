package com.criptografia.servidor_arquivos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"files\"")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    private String filetype;

    @Lob
    private byte[] data;

    private String algorithmUsed;

    private String encryptionKeyBase64;

    private String uploadTimestamp;

    @ManyToOne
    @JoinColumn(name = "uploader_id")
    private User uploader;
}
