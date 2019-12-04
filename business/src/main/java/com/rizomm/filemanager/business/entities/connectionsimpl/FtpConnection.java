package com.rizomm.filemanager.business.entities.connectionsimpl;

import com.rizomm.filemanager.business.entities.Connection;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class FtpConnection extends Connection {
    private int port = 21;
    private String documentRoot = "";
    private String username;
    private String password;

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public Connection closeConnection() {
        return null;
    }
}
