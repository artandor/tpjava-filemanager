package com.rizomm.filemanager.controllers;

import com.rizomm.filemanager.business.entities.connectionsimpl.AmazonS3Connection;
import com.rizomm.filemanager.business.repositories.AmazonS3ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/connections")
public class ConnectionController {

    @Autowired
    private AmazonS3ConnectionRepository amazonS3ConnectionRepository;

    @GetMapping()
    public ResponseEntity<List<AmazonS3Connection>> getAllConnections() {
        return ResponseEntity.ok(amazonS3ConnectionRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<AmazonS3Connection> createConnection(@RequestBody @Validated AmazonS3Connection connection) {
        amazonS3ConnectionRepository.save(connection);
        return ResponseEntity.ok(connection);
    }
}
