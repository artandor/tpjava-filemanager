package com.rizomm.filemanager.business.repositories;

import com.rizomm.filemanager.business.entities.connectionsimpl.AmazonS3Connection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AmazonS3ConnectionRepository extends JpaRepository<AmazonS3Connection, Long> {
    public Optional<AmazonS3Connection> findFirstById(long id);
}
