package com.rizomm.filemanager.business.repositories;

import com.rizomm.filemanager.business.entities.connectionsimpl.FtpConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FtpConnectionRepository extends JpaRepository<FtpConnection, Long> {
    public Optional<FtpConnection> findFirstById(long id);
}
