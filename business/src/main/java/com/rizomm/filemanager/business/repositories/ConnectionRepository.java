package com.rizomm.filemanager.business.repositories;

import com.rizomm.filemanager.business.entities.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    @Override
    List<Connection> findAll();
}
