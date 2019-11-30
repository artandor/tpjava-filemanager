package com.rizomm.filemanager.business.entities.connectionsImpl;

import com.rizomm.filemanager.business.entities.Connection;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class AmazonS3Connection implements Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String accessKey;
    @NotNull
    private String secretKey;
    @NotNull
    private String host;

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public Connection closeConnection() {
        return null;
    }
}
