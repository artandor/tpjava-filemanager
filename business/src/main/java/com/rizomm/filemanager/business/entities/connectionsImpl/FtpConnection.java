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
public class FtpConnection implements Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String protocol = "ftp";

    @NotNull
    private String host;
    private String port = "21";
    private String documentRoot = "";

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public Connection closeConnection() {
        return null;
    }
}
