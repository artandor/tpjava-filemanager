package com.rizomm.filemanager.business.entities.connectionsimpl;

import com.rizomm.filemanager.business.entities.Connection;
import lombok.Data;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class AmazonS3Connection extends Connection {
    @NotNull
    private String accessKey;
    @NotNull
    private String secretKey;
    @NotNull
    private String bucket;

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public Connection closeConnection() {
        return null;
    }
}
