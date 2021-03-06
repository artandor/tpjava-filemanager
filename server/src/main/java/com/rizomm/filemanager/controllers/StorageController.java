package com.rizomm.filemanager.controllers;

import com.rizomm.filemanager.business.entities.connectionsimpl.AmazonS3Connection;
import com.rizomm.filemanager.business.entities.connectionsimpl.FtpConnection;
import com.rizomm.filemanager.business.repositories.AmazonS3ConnectionRepository;
import com.rizomm.filemanager.business.services.AmazonS3Service;
import com.rizomm.filemanager.business.services.FtpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import static com.rizomm.filemanager.business.utils.Utils.convertMultiPartToFile;
import static com.rizomm.filemanager.business.utils.Utils.generateFileName;

@Controller
@RequestMapping("/connections/{connectionId}")
public class StorageController {
    final AmazonS3Service amazonS3Service;

    final AmazonS3ConnectionRepository amazonS3ConnectionRepository;

    final FtpService ftpService;


    public StorageController(AmazonS3Service amazonS3Service, AmazonS3ConnectionRepository amazonS3ConnectionRepository, FtpService ftpService) {
        this.amazonS3Service = amazonS3Service;
        this.amazonS3ConnectionRepository = amazonS3ConnectionRepository;
        this.ftpService = ftpService;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file, @PathVariable long connectionId, Principal principal) {
        Optional<AmazonS3Connection> amazonS3Connection = amazonS3ConnectionRepository.findFirstById(connectionId);
        if (amazonS3Connection.isPresent()) {
            this.amazonS3Service.connection = amazonS3Connection.get();
            if (!this.amazonS3Service.connection.getUserEmail().equals(principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            this.amazonS3Service.initialize();
            return ResponseEntity.status(HttpStatus.CREATED).body(this.amazonS3Service.uploadFile(file));
        }

        Optional<FtpConnection> ftpConnection = ftpService.findFirstById(connectionId);
        if (ftpConnection.isPresent()) {
            this.ftpService.ftpConnection = ftpConnection.get();
            if (!this.ftpService.ftpConnection.getUserEmail().equals(principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            try {
                this.ftpService.initialize();
                File uploadedFile = convertMultiPartToFile(file);
                String fileName = generateFileName(file);
                try {
                    this.ftpService.putFileToPath(fileName, uploadedFile);
                } catch (IOException e) {
                    System.err.println(e.getCause());
                }
                this.ftpService.close();
                return ResponseEntity.status(HttpStatus.CREATED).body(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deleteFile")
    public ResponseEntity<String> deleteFile(@PathVariable long connectionId, @RequestBody String fileName, Principal principal) {
        Optional<AmazonS3Connection> awsConnection = amazonS3ConnectionRepository.findFirstById(connectionId);

        if (awsConnection.isPresent()) {
            this.amazonS3Service.connection = awsConnection.get();
            if (!this.amazonS3Service.connection.getUserEmail().equals(principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            this.amazonS3Service.initialize();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(this.amazonS3Service.deleteFile(fileName));
        }
        return ResponseEntity.notFound().build();
    }
}
