package com.rizomm.filemanager.controllers;

import com.rizomm.filemanager.business.entities.connectionsimpl.AmazonS3Connection;
import com.rizomm.filemanager.business.repositories.AmazonS3ConnectionRepository;
import com.rizomm.filemanager.business.services.AmazonS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/connections/{connectionId}")
public class StorageController {
    @Autowired
    AmazonS3Service amazonS3Service;

    @Autowired
    AmazonS3ConnectionRepository amazonS3ConnectionRepository;

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file, @PathVariable long connectionId) {
        Optional<AmazonS3Connection> connection = amazonS3ConnectionRepository.findFirstById(connectionId);

        if (connection.isPresent()) {
            this.amazonS3Service.connection = connection.get();
            this.amazonS3Service.initializeAmazon();
        }
        return ResponseEntity.ok(this.amazonS3Service.uploadFile(file));
    }
}
