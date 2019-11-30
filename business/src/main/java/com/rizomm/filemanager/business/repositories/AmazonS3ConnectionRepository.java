package com.rizomm.filemanager.business.repositories;

import com.rizomm.filemanager.business.entities.connectionsImpl.AmazonS3Connection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmazonS3ConnectionRepository extends JpaRepository<AmazonS3Connection, Long> {
}
