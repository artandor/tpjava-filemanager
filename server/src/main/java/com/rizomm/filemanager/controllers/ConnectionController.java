package com.rizomm.filemanager.controllers;

import com.rizomm.filemanager.business.entities.Connection;
import com.rizomm.filemanager.business.entities.connectionsimpl.AmazonS3Connection;
import com.rizomm.filemanager.business.entities.connectionsimpl.FtpConnection;
import com.rizomm.filemanager.business.repositories.AmazonS3ConnectionRepository;
import com.rizomm.filemanager.business.repositories.ConnectionRepository;
import com.rizomm.filemanager.business.services.FtpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<AmazonS3Connection> createConnection(@RequestBody @Validated AmazonS3Connection connection, Principal principal) {
        connection.setUserEmail(principal.getName());
        amazonS3ConnectionRepository.save(connection);
        return ResponseEntity.status(HttpStatus.CREATED).body(connection);
    }

    @PostMapping("sftp")
    public ResponseEntity<FtpConnection> createConnection(@RequestBody @Validated FtpConnection connection, Principal principal) {
        connection.setUserEmail(principal.getName());
        this.ftpService.save(connection);
        return ResponseEntity.status(HttpStatus.CREATED).body(connection);
    }

    @PutMapping("aws")
    public ResponseEntity<AmazonS3Connection> updateConnection(@PathVariable long id, @RequestBody @Validated AmazonS3Connection connection, Principal principal) {
        Optional<AmazonS3Connection> connectionToUpdate = this.amazonS3ConnectionRepository.findFirstById(id);
        if (connectionToUpdate.isPresent()) {
            if (!connectionToUpdate.get().getUserEmail().equals(principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else return ResponseEntity.notFound().build();

        connection.setId(id);
        amazonS3ConnectionRepository.save(connection);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(connection);
    }

    @PutMapping("sftp")
    public ResponseEntity<FtpConnection> updateConnection(@PathVariable long id, @RequestBody @Validated FtpConnection connection, Principal principal) {
        Optional<FtpConnection> connectionToUpdate = this.ftpService.findFirstById(id);
        if (connectionToUpdate.isPresent()) {
            if (!connectionToUpdate.get().getUserEmail().equals(principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else return ResponseEntity.notFound().build();

        connection.setId(id);
        this.ftpService.save(connection);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(connection);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteConnection(@PathVariable long id, Principal principal) {
        Optional<AmazonS3Connection> connectionToDelete = this.amazonS3ConnectionRepository.findFirstById(id);
        if (connectionToDelete.isPresent()) {
            if (!connectionToDelete.get().getUserEmail().equals(principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            this.amazonS3ConnectionRepository.delete(connectionToDelete.get());
            return ResponseEntity.noContent().build();
        }

        Optional<FtpConnection> connectionToDelete2 = this.ftpService.findFirstById(id);
        if (connectionToDelete2.isPresent()) {
            if (!connectionToDelete2.get().getUserEmail().equals(principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            this.ftpService.delete(connectionToDelete2.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
