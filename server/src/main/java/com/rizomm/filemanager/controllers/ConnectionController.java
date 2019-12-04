package com.rizomm.filemanager.controllers;

import com.rizomm.filemanager.business.entities.Connection;
import com.rizomm.filemanager.business.entities.connectionsimpl.AmazonS3Connection;
import com.rizomm.filemanager.business.entities.connectionsimpl.FtpConnection;
import com.rizomm.filemanager.business.repositories.AmazonS3ConnectionRepository;
import com.rizomm.filemanager.business.repositories.ConnectionRepository;
import com.rizomm.filemanager.business.services.FtpService;
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
    private final AmazonS3ConnectionRepository amazonS3ConnectionRepository;
    private final FtpService ftpService;
    private final ConnectionRepository connectionRepository;

    public ConnectionController(AmazonS3ConnectionRepository amazonS3ConnectionRepository, FtpService ftpService, ConnectionRepository connectionRepository) {
        this.amazonS3ConnectionRepository = amazonS3ConnectionRepository;
        this.ftpService = ftpService;
        this.connectionRepository = connectionRepository;
    }

    @GetMapping()
    public ResponseEntity<List<Connection>> getAllConnections() {
        List<Connection> result = connectionRepository.findAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping("aws")
    public ResponseEntity<AmazonS3Connection> createConnection(@RequestBody @Validated AmazonS3Connection connection) {
        amazonS3ConnectionRepository.save(connection);
        return ResponseEntity.ok(connection);
    }

    @PostMapping("sftp")
    public ResponseEntity<FtpConnection> createConnection(@RequestBody @Validated FtpConnection connection) {
        this.ftpService.save(connection);
        return ResponseEntity.ok(connection);
    }
}
